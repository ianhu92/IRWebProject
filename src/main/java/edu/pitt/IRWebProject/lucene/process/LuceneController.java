package edu.pitt.IRWebProject.lucene.process;

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

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class LuceneController {
    StandardAnalyzer analyzer;
    Directory dir1;
    Directory dir2;
    IndexWriterConfig config;
    IndexWriter writer = null;
    IndexReader[] readers;
    IndexSearcher searcher;

    public LuceneController() throws IOException {
        // 0. Specify the analyzer for tokenizing text.
        //    The same analyzer should be used for indexing and searching
        analyzer = new StandardAnalyzer();

        dir1 = FSDirectory.open(Paths.get("./src/main/resources/data/zhihuindexweb/"));
        dir2 = FSDirectory.open(Paths.get("./src/main/resources/data/StackOverFlowIndex/"));

        config = new IndexWriterConfig(analyzer);

    }

    public static void main(String[] args) throws Exception {
        LuceneController lucene = new LuceneController();

//        lucene.writetoLucene("StackOverFlow");
        lucene.searchQuery("How to create google");
    }

    private void initReader() throws IOException {
        readers = new IndexReader[2];
        readers[0] = DirectoryReader.open(dir1);
        readers[1] = DirectoryReader.open(dir2);

        searcher = new IndexSearcher(new MultiReader(readers));
    }

    public void searchQuery(String query) throws Exception {
        ScoreDoc[] results = searchDoc(query);

        readSortedResults(results);

        closeReader();
    }

    private void writetoLucene(String type) throws IOException {
        TrecWebReader webReader;

        if (type.equals("zhihu")) {
            writer = new IndexWriter(dir1, config);
            webReader = new TrecWebReader(type);
        }
        else {
            writer = new IndexWriter(dir2, config);
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

    private void readSortedResults(ScoreDoc[] hits) throws IOException {

        // 4. display results
        System.out.println("Found " + hits.length + " hits.");

        List<Map.Entry<Integer, Double>> list = sortDocScore(hits);

        for (Map.Entry<Integer, Double> entry : list) {
            Document d = searcher.doc(entry.getKey());
            Double score = entry.getValue();

            System.out.println("Score:" + score);
            System.out.println("title:" + d.get("title"));
            System.out.println("content:" + d.get("content"));
            System.out.println("votes:" + d.get("votes"));
            System.out.println("Answer:" + d.get("answerContent"));
        }
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
            String title = ques.getTitle();
            String content = ques.getContent();

            for (MyAnswer ans : ques.getAnswers()) {
                Document doc = new Document();

                doc.add(new StoredField("docno", docno));
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