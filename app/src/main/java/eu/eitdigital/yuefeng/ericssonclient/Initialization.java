package eu.eitdigital.yuefeng.ericssonclient;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;



public class Initialization extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);
        sharedPreferences = this.getSharedPreferences("UserInfo",MODE_WORLD_READABLE);
        editor = sharedPreferences.edit();
        if (!sharedPreferences.getString("name","").isEmpty())
        {
            Intent intent = new Intent();
            intent.setClass(Initialization.this,Information.class);
            startActivity(intent);
            Initialization.this.finish();
        }
        Button finishButton = (Button) findViewById(R.id.finishButtom);
        final EditText name =(EditText) findViewById(R.id.name);
        final EditText phone = (EditText) findViewById(R.id.phone);
        final EditText university = (EditText) findViewById(R.id.university);
        final EditText program = (EditText) findViewById(R.id.program);
        final EditText graduation = (EditText) findViewById(R.id.graduationYear);
        final EditText email = (EditText) findViewById(R.id.email);
        assert finishButton != null;
        finishButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (name.getText().toString().isEmpty())
                {
                    Toast.makeText(Initialization.this, "Name is empty!", Toast.LENGTH_LONG).show();
                }
                else if ((phone.getText().toString().isEmpty())||(!isNumeric(phone.getText().toString())))
                {
                    Toast.makeText(Initialization.this, "Phone number is invalid!", Toast.LENGTH_LONG).show();
                }
                else if (university.getText().toString().isEmpty())
                {
                    Toast.makeText(Initialization.this, "University name is invalid!", Toast.LENGTH_LONG).show();
                }
                else if (program.getText().toString().isEmpty())
                {
                    Toast.makeText(Initialization.this, "Program name is invalid!", Toast.LENGTH_LONG).show();
                }
                else if (graduation.getText().toString().length()!=4)
                {
                    Toast.makeText(Initialization.this,"Please input 4-digit year!", Toast.LENGTH_LONG).show();
                }
                else if (!email.getText().toString().contains("@"))
                {
                    Toast.makeText(Initialization.this, "Please input valid email address!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    editor.putString("name", name.getText().toString());
                    editor.putString("phone", phone.getText().toString());
                    editor.putString("university", university.getText().toString());
                    editor.putString("program", program.getText().toString());
                    editor.putString("graduation", graduation.getText().toString());
                    editor.putString("email", email.getText().toString());
                    editor.commit();
                    Intent intent = new Intent();
                    intent.setClass(Initialization.this,Information.class);
                    startActivity(intent);
                    Initialization.this.finish();

                }
            }
        });
    }

    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

}

