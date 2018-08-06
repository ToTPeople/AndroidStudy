//package com.example.lfs.androidstudy;
package com.example.lfs.androidstudy.InternalStorageDemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lfs.androidstudy.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class InternalStorageDemo extends AppCompatActivity {

    private Button saveButton;
    private Button readButton;

    private TextView textView;
    private EditText editText;

    // Is a simple file name.
// Note!! Do not allow the path.
    private String simpleFileName = "note.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_storage_demo);

        this.saveButton = (Button) findViewById(R.id.button_save);
        this.readButton = (Button) findViewById(R.id.button_read);
        this.textView = (TextView) findViewById(R.id.textView);
        this.editText = (EditText) findViewById(R.id.editText);

        this.saveButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        this.readButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                readData();
            }
        });
    }


    private void saveData() {
        String data = this.editText.getText().toString();
        try {
// Open Stream to write file.
            FileOutputStream out = openFileOutput(simpleFileName, MODE_PRIVATE);

            out.write(data.getBytes());
            out.close();
            Toast.makeText(this,"File saved!",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this,"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void readData() {
        try {
// Open stream to read file.
            FileInputStream in = openFileInput(simpleFileName);

            BufferedReader br= new BufferedReader(new InputStreamReader(in));

            StringBuilder sb= new StringBuilder();
            String s= null;
            while((s= br.readLine())!= null){
                sb.append(s).append("\n");
            }
            this.textView.setText(sb.toString());

        } catch (Exception e) {
            Toast.makeText(this,"Error:"+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
