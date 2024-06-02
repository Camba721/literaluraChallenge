package com.alura.literalura.main;

import com.alura.literalura.model.AuthorInfo;
import com.alura.literalura.model.Book;
import com.alura.literalura.model.Data;
import com.alura.literalura.model.Languages;
import com.alura.literalura.repository.BookRepository;
import com.alura.literalura.services.DataConvert;
import com.alura.literalura.services.RequestAPI;

import java.util.*;

public class MainTasks {
    
    private Scanner scanner = new Scanner(System.in);
    private RequestAPI requestAPI = new RequestAPI();
    private DataConvert dataConvert = new DataConvert();
    private BookRepository repository;
    private final String BASE_URL = "https://gutendex.com/books/";
    private List<Book> books;
    private String bookSelected;
    
    public MainTasks ( BookRepository repository ) {
        this.repository = repository;
    }
    
    public void showMenu () {
        var option = - 1;
        while ( option != 0 ) {
            var menu = """
                      === LITERALURA BASADA EN LA API DEL PROYECTO GUTENDEX ===
                    
                    1 - Buscar un libro por su Título
                    2 - Listar libros registrados (Buscados con anterioridad)
                    3 - Listar autores registrados (Buscados con anterioridad)
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma (Español, Ingles, Portugues, Frances, Italiano)
                    0 - Salir
                    
                    == Elija una opcion ==
                    """;
            System.out.println(menu);
            option = scanner.nextInt();
            scanner.nextLine();
            
            switch (option) {
                case 1:
                    getBookData();
                    break;
                case 2:
                    showStoredBooks();
                    break;
                case 3:
                    authorsListStored();
                    break;
                case 4:
                    getAuthorYear();
                    break;
                case 5:
                    findBooksByLanguages();
                    break;
            }
        }
    }
    
    
    private String getDataFromUser () {
        System.out.println("Introduzca el nombre que desea buscar");
        bookSelected = scanner.nextLine();
        return bookSelected;
    }
    
    // Función para obtener los datos del libro de la API
    private Data getBookDataFromAPI ( String bookTitle ) {
        var json = requestAPI.getData(BASE_URL + "?search=%20" + bookTitle.replace(" ", "+"));
        var data = dataConvert.getData(json, Data.class);
        
        return data;
    }
    
    private Optional<Book> getBookInfo ( Data bookData, String bookTitle ) {
        Optional<Book> books = bookData.results().stream()
                .filter(l -> l.title().toLowerCase().contains(bookTitle.toLowerCase()))
                .map(b -> new Book(b.title(), b.languages(), b.downloads(), b.authors()))
                .findFirst();
        return books;
    }
    
    private void buscarSerieWeb () {
        String title = getDataFromUser();
        Data datos = getBookDataFromAPI(title);
        Book book = new Book(datos.results());
        repository.save(book);
        
        System.out.println(book);
    }
    
    private Optional<Book> getBookData () {
        String bookTitle = getDataFromUser();
        Data bookInfo = getBookDataFromAPI(bookTitle);
        Optional<Book> book = getBookInfo(bookInfo, bookTitle);
        
        if ( book.isPresent() ) {
            var b = book.get();
            repository.save(b);
            System.out.println(b);
        } else {
            System.out.println("\nLIBRO NO HALLADO\n");
        }
        
        return book;
    }

    private void showStoredBooks () {
        books = repository.findAll();
        
        books.stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .forEach(System.out::println);
    }

    private void authorsListStored () {
        List<AuthorInfo> authors = repository.getAuthorsInfo();
        
        authors.stream()
                .sorted(Comparator.comparing(AuthorInfo::getName))
                .forEach(a -> System.out.printf("Author: %s Born: %s Death: %s\n",
                        a.getName(), a.getBirthYear(), a.getDeathYear()));
    }

    
    public void getAuthorYear () {
        System.out.println("Intorduzca el año desde el cual desea saber si el author se encontrba vivo");
        int date = scanner.nextInt();
        scanner.nextLine();
        
        List<AuthorInfo> authorInfos = repository.getAuthorLiveAfter(date);
        
        authorInfos.stream()
                .sorted(Comparator.comparing(AuthorInfo::getName))
                .forEach(a -> System.out.printf("Author: %s Born: %s Death: %s\n",
                        a.getName(), a.getBirthYear(), a.getDeathYear()));
    }
    

    public void findBooksByLanguages () {
        String languagesList = """
                Elija entre las opciones del idioma del libro que desea seleccionar:
                
                en - Ingles
                es - Español
                fr - Frances
                it - Italiano
                pt - Portugues
                
                """;
        System.out.println(languagesList);
        String text =  scanner.nextLine();
        
        var language = Languages.fromString(text);

        List<Book> bookLanguage = repository.findByLanguages(language);

        bookLanguage.stream()
                .forEach(System.out::println);
    }
}
