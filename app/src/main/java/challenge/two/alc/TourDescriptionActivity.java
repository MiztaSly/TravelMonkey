package challenge.two.alc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class TourDescriptionActivity extends AppCompatActivity {
    private ImageView descrpImageView;
    private TextView descTitle;
    private TextView descPrice;
    private TextView overview;
    TourDeal deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_description);

        descrpImageView = findViewById(R.id.discrpt_imgV);
        descTitle = findViewById(R.id.descrTitle);
        descPrice = findViewById(R.id.descripPrice);
        overview = findViewById(R.id.descOverview);

        //retrieve deal
        Intent intent = getIntent();
        TourDeal deal = (TourDeal) intent.getSerializableExtra("Deal");
//        if (deal == null) {
//            deal = new TourDeal();
//        }
        this.deal = deal;
        descTitle.setText(deal.getTitle());
        overview.setText(deal.getDescription());
        descPrice.setText(deal.getPrice());
        showImage(deal.getImageUrl());

    }

    public void showImage(String url) {
        if (url != null && url.isEmpty() == false) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get().load(url)
                    .into(descrpImageView);
            Toast.makeText(this, "Image Loaded successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void openBooking(View view) {
        startActivity(new Intent(this, BookingActivity.class));
    }
}
