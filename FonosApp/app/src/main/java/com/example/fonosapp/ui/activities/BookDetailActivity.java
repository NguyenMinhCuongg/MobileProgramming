package com.example.fonosapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.fonosapp.R;
import com.example.fonosapp.data.models.Book;
import com.example.fonosapp.data.remote.BookManager;
import com.example.fonosapp.utils.AppNavigator;

public class BookDetailActivity extends AppCompatActivity {

    private BookManager bookManager;
    private ImageView ivBookCover;
    private TextView tvBookTitle, tvAuthor, tvSummaryContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_detail);

        bookManager = new BookManager();
        initViews();

        String bookId = getIntent().getStringExtra("BOOK_ID");
        if (bookId != null) {
            loadBookDetails(bookId);
        }

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        Button btnListen = findViewById(R.id.btnListen);
        btnListen.setOnClickListener(v -> {
            // Implicit Intent: Chia sẻ thông tin sách
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Đang nghe cuốn sách: " + tvBookTitle.getText());
            startActivity(Intent.createChooser(shareIntent, "Chia sẻ sách"));
            
            // Navigate mượt mà tới Player
            AppNavigator.navigateTo(this, PlayerActivity.class, false);
        });
    }

    private void initViews() {
        ivBookCover = findViewById(R.id.ivBookCover);
        tvBookTitle = findViewById(R.id.tvBookTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvSummaryContent = findViewById(R.id.tvSummaryContent);
    }

    private void loadBookDetails(String bookId) {
        bookManager.fetchBookById(bookId, (book, error) -> {
            if (error != null) {
                Toast.makeText(this, "Lỗi: " + error, Toast.LENGTH_LONG).show();
            } else if (book != null) {
                displayBook(book);
            }
            return kotlin.Unit.INSTANCE;
        });
    }

    private void displayBook(Book book) {
        tvBookTitle.setText(book.getTitle());
        tvSummaryContent.setText(book.getDescription());
        
        if (book.getCoverUrl() != null) {
            Glide.with(this)
                 .load(book.getCoverUrl())
                 .placeholder(R.drawable.ic_person_placeholder)
                 .into(ivBookCover);
        }

        tvAuthor.setText("Tác giả ID: " + book.getAuthorId());
    }
}
