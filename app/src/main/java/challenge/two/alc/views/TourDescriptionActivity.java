package challenge.two.alc.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import challenge.two.alc.R;
import challenge.two.alc.model.TourDeal;
import challenge.two.alc.views.BookingActivity;

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

        this.deal = deal;
        descTitle.setText(deal.getTitle());
        overview.setText(deal.getDescription());
        descPrice.setText(deal.getPrice());
        showImage(deal.getImageUrl());

    }

    //disable menu items, user can't add or edit tours
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        menu.findItem(R.id.delete_menu).setVisible(false);
        menu.findItem(R.id.save_menu).setVisible(false);

        return true;
    }


    public void showImage(String url) {
        if (url != null && url.isEmpty() == false) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get().load(url)
                    .into(descrpImageView);
            Toast.makeText(this, getString(R.string.image_loaded), Toast.LENGTH_SHORT).show();
        }
    }

    public void openBooking(View view) {
        startActivity(new Intent(this, BookingActivity.class));
    }
}
