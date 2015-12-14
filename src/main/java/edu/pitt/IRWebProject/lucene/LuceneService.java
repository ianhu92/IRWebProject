package edu.pitt.IRWebProject.lucene;

/**
 * Created by zhaojun on 12/10/15.
 */

import edu.pitt.IRWebProject.stackoverflow.parser.MyAnswer;
import edu.pitt.IRWebProject.stackoverflow.parser.MyQuestion;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Service
public class LuceneService {
    StandardAnalyzer analyzer;
    Directory dir1;
    Directory dir2;
    IndexWriterConfig config1;
    IndexWriterConfig config2;
    IndexWriter writer = null;
    IndexReader[] readers;
    IndexSearcher searcher;

    public LuceneService() throws IOException {
        // 0. Specify the analyzer for tokenizing text.
        //    The same analyzer should be used for indexing and searching
        analyzer = new StandardAnalyzer();

        dir1 = FSDirectory.open(Paths.get("./src/main/resources/data/ZhihuIndex/"));
        dir2 = FSDirectory.open(Paths.get("./src/main/resources/data/StackOverFlowIndex/"));

        config1 = new IndexWriterConfig(analyzer);
        config2 = new IndexWriterConfig(analyzer);

    }

    public static void main(String[] args) throws Exception {
        LuceneService lucene = new LuceneService();

//        lucene.writetoLucene("StackOverFlow");
//        lucene.writetoLucene("zhihu");

        String json = lucene.searchQuery("How to create google");
        System.out.println(json);
    }

    private void initReader() throws IOException {
        readers = new IndexReader[2];
        readers[0] = DirectoryReader.open(dir1);
        readers[1] = DirectoryReader.open(dir2);

        searcher = new IndexSearcher(new MultiReader(readers));
    }

    public String searchQuery(String query) throws Exception {
        ScoreDoc[] results = searchDoc(query);

        String result = readSortedResults(results);

        closeReader();

        return result;
    }

    @SuppressWarnings("unused")
    private void writetoLucene(String type) throws IOException {
        TrecWebReader webReader;

        if (type.equals("zhihu")) {
            writer = new IndexWriter(dir1, config1);
            webReader = new TrecWebReader(type);
        }
        else {
            writer = new IndexWriter(dir2, config2);
            webReader = new TrecWebReader(type);
        }

        Map<String, MyQuestion> map;

        while ((map = webReader.nextQuestion()) != null) {
            addDoc(writer, map);
        }

        writer.close();
    }

    private ScoreDoc[] searchDoc(String query) throws Exception {
        // the "title" arg specifies the default field to use
        // when no field is explicitly specified in the query.
        initReader();

        String[] fields = new String[]{"title", "content", "answerContent"};
        MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(fields, analyzer);
        Query q = multiFieldQueryParser.parse(query);

        int hitsPerPage = 150;

        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        return hits;
    }

    private String readSortedResults(ScoreDoc[] hits) throws IOException {
        JSONArray jsonArray = new JSONArray();



        StringBuilder sb = new StringBuilder();

        // 4. display results
        System.out.println("Found " + hits.length + " hits.");

        List<Map.Entry<Integer, Double>> list = sortDocScore(hits);

//        sb.append("[");

        for (Map.Entry<Integer, Double> entry : list) {
            JSONObject jsonObject = new JSONObject();
            Document d = searcher.doc(entry.getKey());
            Double score = entry.getValue();
            String source = getSource(d.get("docno"));

//            System.out.println("Score:" + score);
            jsonObject.put("title", d.get("title"));
            jsonObject.put("url", d.get("url"));
            jsonObject.put("soucre", source);
            jsonObject.put("answer", d.get("answerContent"));
            jsonObject.put("votes", d.get("votes"));

            jsonArray.put(jsonObject);
//            sb.append("{");
//            sb.append("\"title\":\"" + d.get("title") + "\" , ");
//            sb.append("\"url\":\"" + d.get("url") + "\" , ");
//            sb.append("\"source\":\"" + source + "\" , ");
//            sb.append("\"answer\":\"" + d.get("answerContent") + "\" , ");
//            sb.append("\"votes\":\"" + d.get("votes") + "\" ");
//            sb.append("}, ");

//            System.out.println("DOCNO:" + d.get("docno"));
//            System.out.println("title:" + d.get("title"));
//            System.out.println("content:" + d.get("content"));
//            System.out.println("votes:" + d.get("votes"));
//            System.out.println("Answer:" + d.get("answerContent"));
        }

//        sb.setLength(sb.length() - 1);
//        sb.append("]");

        return jsonArray.toString();
    }

    private String getSource(String docno) {
        return docno.charAt(0) == 'z' ? "zhihu" : "stackoverflow";
    }

    private List<Map.Entry<Integer, Double>> sortDocScore(ScoreDoc[] hits) throws IOException {
        TreeMap<Integer, Double> treeMap = new TreeMap<Integer,Double>();

        for (ScoreDoc hit : hits) {
            Document d = searcher.doc(hit.doc);
            Double votes = Double.valueOf(d.get("votes"));
            Double totalVotes = Double.valueOf(d.get("totalVotes"));
            votes = votes < 0 ? 0 : votes;
            totalVotes = totalVotes < 0 ? 0 : totalVotes;

            // Calculate Final Score
//            Double Origin = hit.score * (Double.valueOf(d.get("votes")) + 1);

            Double newScore = hit.score * (votes + 1) / (totalVotes + 1);

            treeMap.put(hit.doc, newScore);
        }

        List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(treeMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        return list;
    }

    private void closeReader() throws IOException {
        for (IndexReader reader : readers ) {
            reader.close();
        }
    }

    private void addDoc(IndexWriter writer, Map<String, MyQuestion> map) throws IOException {
        for (Map.Entry<String, MyQuestion> entry : map.entrySet()) {
            String docno = entry.getKey();

            MyQuestion ques = entry.getValue();
            String url = ques.getUrl();
            String title = ques.getTitle();
            String content = ques.getContent();

            for (MyAnswer ans : ques.getAnswers()) {
                Document doc = new Document();

                doc.add(new StoredField("docno", docno));
                doc.add(new StoredField("url", url));
                doc.add(new TextField("title", title, Field.Store.YES));
                doc.add((new StoredField("totalVotes", ques.getTotalvotes())));
                if (content != null && content.length() > 0) {
                    doc.add(new TextField("content", content, Field.Store.YES));
                }

                doc.add((new StoredField("votes", ans.getVotes())));

                doc.add((new TextField("answerContent", ans.getContent(), Field.Store.YES)));

                writer.addDocument(doc);
            }
        }
    }
}