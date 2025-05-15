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
import java.util.Scanner;

public class Buscador {
    public static void main(String[] args) {
        String diretorioIndice = "indice";

        try (Directory diretorio = FSDirectory.open(Paths.get(diretorioIndice));
             IndexReader leitor = DirectoryReader.open(diretorio);
             Scanner scanner = new Scanner(System.in)) {

            IndexSearcher buscador = new IndexSearcher(leitor);
            Analyzer analisador = new StandardAnalyzer();
            QueryParser parser = new QueryParser("conteudo", analisador);

            System.out.print("Digite :q para sair.");

            while (true) {
                System.out.print("\n#############################################\nConsulta: ");
                String entrada = scanner.nextLine();

                if (":q".equalsIgnoreCase(entrada)) {
                    break;
                }

                try {
                    Query consulta = parser.parse(entrada);
                    TopDocs resultados = buscador.search(consulta, 10);

                    System.out.println("\nTotal de documentos encontrados: " + resultados.totalHits.value());

                    // Obter StoredFields uma vez para todas as recuperações
                    StoredFields storedFields = leitor.storedFields();

                    for (ScoreDoc scoreDoc : resultados.scoreDocs) {
                        Document doc = storedFields.document(scoreDoc.doc);

                        System.out.println("\nArquivo: " + doc.get("nome_arquivo"));
                        System.out.println("Caminho: " + doc.get("caminho"));
                        System.out.println("Score: " + scoreDoc.score);

                        String conteudo = doc.get("conteudo");
                        if (conteudo != null && !conteudo.isEmpty()) {
                            System.out.println("Trecho relevante: " +
                                    conteudo.substring(0, Math.min(100, conteudo.length())) + "...");
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Erro na consulta: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}