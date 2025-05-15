package com.dcc;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

public class Buscador {
    public static void main(String[] args) {
        try {
            // Diretório onde o índice está armazenado
            Directory diretorio = FSDirectory.open(Paths.get("indice"));

            // Leitor do índice
            IndexReader leitor = DirectoryReader.open(diretorio);

            // Searcher para realizar buscas
            IndexSearcher buscador = new IndexSearcher(leitor);

            // Analisador padrão
            Analyzer analisador = new StandardAnalyzer();

            // Parser da consulta
            QueryParser parser = new QueryParser("conteudo", analisador);

            // Consulta
            Query consulta = parser.parse("Lucene");

            // Executando a busca
            TopDocs resultados = buscador.search(consulta, 10);

            System.out.println("Total de documentos encontrados: " + resultados.totalHits.value());

            // Obtendo os campos armazenados
            StoredFields storedFields = leitor.storedFields();

            // resultados
            for (ScoreDoc scoreDoc : resultados.scoreDocs) {
                Document doc = storedFields.document(scoreDoc.doc);
                System.out.println("Conteúdo: " + doc.get("conteudo"));
            }

            leitor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
