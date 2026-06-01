package com.example.fonosapp;

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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.bumptech.glide.Glide;

public class BookDetailActivity extends AppCompatActivity {

    private BookManager bookManager;
    private ImageView ivBookCover;
    private TextView tvBookTitle, tvAuthor, tvSummaryContent, tvRating;
    private View progressBar;

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
        } else {
            Toast.makeText(this, "Không tìm thấy ID sách", Toast.LENGTH_SHORT).show();
        }

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        Button btnListen = findViewById(R.id.btnListen);
        btnListen.setOnClickListener(v -> {
            Intent intent = new Intent(BookDetailActivity.this, PlayerActivity.class);
            startActivity(intent);
        });
    }

    private void initViews() {
        ivBookCover = findViewById(R.id.ivBookCover);
        tvBookTitle = findViewById(R.id.tvBookTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvSummaryContent = findViewById(R.id.tvSummaryContent);
        // Lưu ý: tvRating và progressBar cần được thêm vào XML nếu chưa có, 
        // hoặc tôi sẽ dùng các ID hiện có trong activity_book_detail.xml
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
        
        // Load ảnh bằng Glide
        if (book.getCoverUrl() != null) {
            Glide.with(this)
                 .load(book.getCoverUrl())
                 .placeholder(R.drawable.ic_person_placeholder)
                 .into(ivBookCover);
        }

        // Tạm thời hiển thị ID tác giả (cần fetch thêm Author để hiện tên đẹp hơn)
        tvAuthor.setText("Tác giả ID: " + book.getAuthorId());
    }
}
