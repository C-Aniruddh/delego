package clstr.delego;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.OnClick;
import butterknife.Bind;
import butterknife.ButterKnife;
import clstr.delego.helpers.DocumentHelper;
import clstr.delego.helpers.IntentHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class AddDelegate extends AppCompatActivity {

    @Bind(R.id.user_imageview)
    ImageView uploadImage;
    @Bind(R.id.userNameField)
    EditText nameField;
    @Bind(R.id.userContactNumber)
    EditText contactField;
    @Bind(R.id.userDOBField)
    EditText dobField;
    @Bind(R.id.userEmailId)
    EditText emailField;

    protected File chosenFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delegate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Done!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri returnUri;

        if (requestCode != IntentHelper.FILE_PICK) {
            return;
        }

        if (resultCode != RESULT_OK) {
            return;
        }

        returnUri = data.getData();
        String filePath = DocumentHelper.getPath(this, returnUri);
        //Safety check to prevent null pointer exception
        if (filePath == null || filePath.isEmpty()) return;
        chosenFile = new File(filePath);

                /*
                    Picasso is a wonderful image loading tool from square inc.
                    https://github.com/square/picasso
                 */
        Picasso.with(getBaseContext())
                .load(chosenFile)
                .placeholder(R.drawable.mountains)
                .fit()
                .into(uploadImage);

    }

    @OnClick(R.id.user_imageview)
    public void onChooseImage() {
        nameField.clearFocus();
        emailField.clearFocus();
        dobField.clearFocus();
        contactField.clearFocus();
        IntentHelper.chooseFileIntent(this);
    }

}
