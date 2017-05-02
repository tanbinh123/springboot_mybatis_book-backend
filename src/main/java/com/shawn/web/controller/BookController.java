package com.shawn.web.controller;


import com.shawn.constant.PageConstant;
import com.shawn.constant.ResourceNameConstant;
import com.shawn.model.dto.PaginatedResult;
import com.shawn.model.entity.Book;
import com.shawn.service.BookService;
import com.shawn.util.PageUtil;
import com.shawn.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * @author Xiaoyue Xiao
 */
@RestController
@RequestMapping("/books")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<?> getBooks(@RequestParam(value = "page", required = false) String pageString,//当前页
                                      @RequestParam(value = "per_page", required = false) String perPageString) {
        // Parse request parameters
        int page = PageUtil.parsePage(pageString, PageConstant.PAGE);
        int perPage = PageUtil.parsePerPage(perPageString, PageConstant.PER_PAGE);
        PaginatedResult f = new PaginatedResult();
        f.setData(bookService.getBooksByPage(page, perPage));//数据
        f.setCurrentPage(page);//当前页
        f.setTotalPage(bookService.getTotalPage(perPage));//总页数
        return ResponseEntity
                .ok(f);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable Long bookId) {
        ResourceNotFoundException f = new ResourceNotFoundException();
        f.setResourceName(ResourceNameConstant.BOOK);
        f.setId(bookId);
        return bookService
                .getBookById(bookId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> f);
    }

    @PostMapping("/saveBook")
    public ResponseEntity<?> postBook(@RequestBody Book book) {
        bookService.saveBook(book);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(book.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(book);

    }

    @PutMapping("/{bookId}")
    public ResponseEntity<?> putBook(@PathVariable Long bookId, @RequestBody Book book) {
        assertBookExist(bookId);

        bookService.modifyBookOnNameById(book.setId(bookId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(book);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId) {
        assertBookExist(bookId);

        bookService.deleteBookById(bookId);

        return ResponseEntity
                .noContent()
                .build();
    }

    /********************************** HELPER METHOD **********************************/
    private void assertBookExist(Long bookId) {
        ResourceNotFoundException rf = new ResourceNotFoundException();
        rf.setResourceName(ResourceNameConstant.BOOK);
        rf.setId(bookId);
        bookService
                .getBookById(bookId)
                .orElseThrow(() -> rf);
    }

}
