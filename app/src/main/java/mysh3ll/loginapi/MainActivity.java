package mysh3ll.loginapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView userProfile, userPasswor, userOrder, userLogout;
    private Button btnUser, btnPassword, btnOrder, btnLogout;
//    private ImageView imageBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userProfile = (TextView) findViewById(R.id.text_profile);
        userPasswor = (TextView) findViewById(R.id.text_password);
        userOrder = (TextView) findViewById(R.id.text_order);
        userLogout = (TextView) findViewById(R.id.text_logout);

        btnUser = (Button) findViewById(R.id.btn_profile);
        btnPassword = (Button) findViewById(R.id.btn_password);
        btnOrder = (Button) findViewById(R.id.btn_order);
        btnLogout = (Button) findViewById(R.id.btn_logout);

//        imageBackground = (ImageView) findViewById(R.id.image_background);

        //----------------LOGOUT----------------------------------
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //----------------USER PROFILE----------------------------------
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        //----------------USER PASSWORD----------------------------------
        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserPasswordActivity.class);
                startActivity(intent);
            }
        });

        //----------------USER ORDER----------------------------------
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserOrderActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent returnIntent = getIntent();

        switch (v.getId()) {

            case R.id.btn_logout:
                setResult(RESULT_OK,returnIntent);
                finish();
                break;
            default:
                break;
        }
    }
}
