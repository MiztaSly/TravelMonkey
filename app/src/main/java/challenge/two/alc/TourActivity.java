package challenge.two.alc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class TourActivity extends AppCompatActivity  {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private static final int PICTURE_RESULT = 1;
    EditText txtTitle, txtDescription, txtPrice;

    public static FirebaseStorage mStorage;
    public static StorageReference mStorageRef;
    TourDeal deal;
    ImageView imageView;
    Button saveTour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        txtTitle =  findViewById(R.id.txt_title);
        txtDescription = findViewById(R.id.txt_description);
        txtPrice = findViewById(R.id.txt_price);
        imageView = (ImageView) findViewById(R.id.imageView);
        saveTour = findViewById(R.id.btn_saveTour); //do i still need to cast to button?


        Button btnImage = findViewById(R.id.btn_image);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });


        //retrieve deal
        Intent intent = getIntent();
        TourDeal deal = (TourDeal) intent.getSerializableExtra("Deal");
        if (deal == null) {
            deal = new TourDeal();
        }
        this.deal = deal;
        txtTitle.setText(deal.getTitle());
        txtDescription.setText(deal.getDescription());
        txtPrice.setText(deal.getPrice());
        showImage(deal.getImageUrl());

    }



    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(intent.createChooser(intent,"Insert Picture"), PICTURE_RESULT);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu:
                saveTour();
                Toast.makeText(this, getString(R.string.tour_saved), Toast.LENGTH_SHORT).show();
                clean();
                backToList();
                return true;

            case R.id.delete_menu:
                deleteTour();
                Toast.makeText(this, "Deal Deleted", Toast.LENGTH_SHORT).show();
                backToList();
                return true;

                default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        if (FirebaseUtil.isAdmin) {
            menu.findItem(R.id.delete_menu).setVisible(true);
            menu.findItem(R.id.save_menu).setVisible(true);

        } else {
            menu.findItem(R.id.delete_menu).setVisible(false);
            menu.findItem(R.id.save_menu).setVisible(false);

        }
        return true;
    }

    //save deal and also check if already exist
    private void saveTour() {
        deal.setTitle(txtTitle.getText().toString());
        deal.setDescription(txtDescription.getText().toString());
        deal.setPrice(txtPrice.getText().toString());
        if (deal.getId()==null) {
            mDatabaseReference.push().setValue(deal);
        } else {
            mDatabaseReference.child(deal.getId()).setValue(deal);
        }


   }

   private void deleteTour(){
        if (deal == null) {
            Toast.makeText(this, getString(R.string.saveDeal_warning), Toast.LENGTH_SHORT).show();
            return;

        } else {
            mDatabaseReference.child(deal.getId()).removeValue();
        }

   }

    private void clean(){
        txtTitle.setText("");
        txtPrice.setText("");
        txtDescription.setText(" ");
        txtTitle.requestFocus();
    }



    private void backToList(){
       Intent intent = new Intent(this, ListActivity.class);
       startActivity(intent);
    }

    private void enableEditTexts(boolean isEnabled) {
        txtTitle.setEnabled(isEnabled);
        txtDescription.setEnabled(isEnabled);
        txtPrice.setEnabled(isEnabled);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICTURE_RESULT && resultCode == RESULT_OK) {
           Uri imageUri= data.getData();
           final StorageReference reference = FirebaseUtil.mStorageRef.child(imageUri.getLastPathSegment());
           reference.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
               @Override
               public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                  if (!task.isSuccessful()) {
                      throw task.getException();
                  }
                  return reference.getDownloadUrl();
               }
           }).addOnCompleteListener(new OnCompleteListener<Uri>() {
               @Override
               public void onComplete(@NonNull Task<Uri> task) {
                   if (task.isSuccessful()) {
                       Uri downloadUri = task.getResult();
                       String url = downloadUri.toString();
                       //Log.e(TAG, "then: " + downloadUri.toString());
                       deal.setImageUrl(url);
                       showImage(url);


                   }
               }
           });

            Picasso.get().load(imageUri).into(imageView);
        }


    }

    public void showImage(String url) {
        if (url != null && url.isEmpty() == false) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get().load(url).resize(width, width*2/3)
                    .into(imageView);
            Toast.makeText(this, getString(R.string.image_load_success), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {

        super.onResume();



    }
}
