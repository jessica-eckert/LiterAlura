package com.alura.literalura.catalogo;

import com.alura.literalura.services.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleApp implements CommandLineRunner {

    @Autowired
    private LivroService livroService;

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n--- Catálogo de Livros ---");
            System.out.println("1. Carregar livros da API");
            System.out.println("2. Buscar livros por idioma");
            System.out.println("3. Buscar livros por autor");
            System.out.println("4. Buscar livros por título");
            System.out.println("5. Exibir todos os livros");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();  // Consumir o '\n'

            switch (opcao) {
                case 1 -> livroService.carregarLivrosDaAPI();
                case 2 -> {
                    System.out.print("Digite o idioma: ");
                    String idioma = scanner.nextLine();
                    var livrosPorIdioma = livroService.buscarPorIdioma(idioma);
                    if (livrosPorIdioma.isEmpty()) {
                        System.out.println("Nenhum livro encontrado para o idioma: " + idioma);
                    } else {
                        livrosPorIdioma.forEach(System.out::println);
                    }
                }
                case 3 -> {
                    System.out.print("Digite o autor: ");
                    String autor = scanner.nextLine();
                    var livrosPorAutor = livroService.buscarPorAutor(autor);
                    if (livrosPorAutor.isEmpty()) {
                        System.out.println("Nenhum livro encontrado para o autor: " + autor);
                    } else {
                        livrosPorAutor.forEach(System.out::println);
                    }
                }
                case 4 -> {
                    System.out.print("Digite o título: ");
                    String titulo = scanner.nextLine();
                    var livrosPorTitulo = livroService.buscarPorTitulo(titulo);
                    if (livrosPorTitulo.isEmpty()) {
                        System.out.println("Nenhum livro encontrado com o título: " + titulo);
                    } else {
                        livrosPorTitulo.forEach(System.out::println);
                    }
                }
                case 5 -> {
                    var todosOsLivros = livroService.buscarPorAutor(""); // Retorna todos os livros
                    if (todosOsLivros.isEmpty()) {
                        System.out.println("Nenhum livro cadastrado no sistema.");
                    } else {
                        todosOsLivros.forEach(System.out::println);
                    }
                }
                case 6 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 6);
    }
}


