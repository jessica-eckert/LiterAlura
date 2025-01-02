package com.alura.literalura.services;


import com.alura.literalura.models.Livro;
import com.alura.literalura.repositories.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Transactional
    public void carregarLivrosDaAPI() {
        String url = "https://gutendex.com/books";
        RestTemplate restTemplate = new RestTemplate();

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("results")) {
                List<Map<String, Object>> livros = (List<Map<String, Object>>) response.get("results");
                livros.forEach(this::processarLivroDaAPI);
            } else {
                System.out.println("Resposta da API está vazia ou não contém resultados.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar livros da API: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void processarLivroDaAPI(Map<String, Object> livroData) {
        try {
            Livro livro = new Livro();
            livro.setTitulo((String) livroData.getOrDefault("title", "Título Desconhecido"));

            List<Map<String, Object>> authors = (List<Map<String, Object>>) livroData.get("authors");
            if (authors != null && !authors.isEmpty()) {
                livro.setAutor((String) authors.get(0).getOrDefault("name", "Autor Desconhecido"));
            } else {
                livro.setAutor("Autor Desconhecido");
            }

            String idioma = (String) livroData.get("language");
            livro.setIdioma(idioma != null ? idioma : "Idioma Desconhecido");
            livro.setUrl((String) livroData.getOrDefault("url", "URL Não Disponível"));

            livroRepository.save(livro);
            System.out.println("Livro salvo: " + livro.getTitulo());
        } catch (Exception e) {
            System.err.println("Erro ao processar livro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Livro> buscarPorIdioma(String idioma) {
        return livroRepository.findByIdioma(idioma);
    }

    public List<Livro> buscarPorAutor(String autor) {
        return livroRepository.findByAutorContainingIgnoreCase(autor);
    }

    /**
     * Busca livros por título.
     *
     * @param titulo Título ou parte do título do livro a ser pesquisado.
     * @return Lista de livros correspondentes ao título.
     */
    public List<Livro> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }
}




