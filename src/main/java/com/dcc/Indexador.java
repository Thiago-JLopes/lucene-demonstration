package com.dcc;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Indexador {
    public static void main(String[] args) {

        String diretorioIndice = "indice";
        String diretorioDocumentos = "documentos";

        try {
            // diretório de índice
            Files.createDirectories(Paths.get(diretorioIndice));

            // Configurar o indexador
            Directory diretorio = FSDirectory.open(Paths.get(diretorioIndice));
            //Analyzer analisador = new StandardAnalyzer();
            Analyzer analisador = new StopAnalyzer(EnglishAnalyzer.ENGLISH_STOP_WORDS_SET);
            IndexWriterConfig config = new IndexWriterConfig(analisador);


            //config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

            try (IndexWriter escritor = new IndexWriter(diretorio, config)) {
                Files.walk(Paths.get(diretorioDocumentos))
                        .filter(Files::isRegularFile)
                        .filter(p -> p.toString().toLowerCase().endsWith(".txt"))
                        .forEach(p -> indexarDocumento(escritor, p));

                System.out.println("Indexação concluída! Documentos indexados.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void indexarDocumento(IndexWriter escritor, Path arquivo) {
        try {
            String conteudo = new String(Files.readAllBytes(arquivo));
            String nomeArquivo = arquivo.getFileName().toString();

            Document documento = new Document();
            documento.add(new TextField("conteudo", conteudo, Field.Store.YES));
            documento.add(new TextField("nome_arquivo", nomeArquivo, Field.Store.YES));
            documento.add(new TextField("caminho", arquivo.toString(), Field.Store.YES));

            escritor.addDocument(documento);
            System.out.println("Indexado: " + nomeArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao indexar " + arquivo + ": " + e.getMessage());
        }
    }
}