package com.dcc;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class Indexador {
    public static void main(String[] args) {
        try {
            // Diretório onde o índice será armazenado
            Directory diretorio = FSDirectory.open(Paths.get("indice"));

            // Analisador padrão do Lucene
            Analyzer analisador = new StandardAnalyzer();

            // Configuração do IndexWriter
            IndexWriterConfig config = new IndexWriterConfig(analisador);
            IndexWriter escritor = new IndexWriter(diretorio, config);

            // Criando um documento
            Document documento = new Document();
            documento.add(new TextField("conteudo", "O Apache Lucene é uma biblioteca de busca textual poderosa e eficiente.", Field.Store.YES));

            // Adicionando o documento ao índice
            escritor.addDocument(documento);

            // Fechando o escritor
            escritor.close();

            System.out.println("Indexação concluída com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
