package com.bordag.legislative;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class BillEdit extends Activity {
    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_edit);
        setTitle("edit_bill");
        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);
        Button confirmButton = (Button) findViewById(R.id.confirm);
        mRowId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString(BillDbAdapter.KEY_TITLE);
            String body = extras.getString(BillDbAdapter.KEY_BODY);
            mRowId = extras.getLong(BillDbAdapter.KEY_ROWID);

            if (title != null) {
                mTitleText.setText(title);
            }
            if (body != null) {
                mBodyText.setText(body);
            }
        }

        confirmButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();

                        bundle.putString(BillDbAdapter.KEY_TITLE, mTitleText.getText().toString());
                        bundle.putString(BillDbAdapter.KEY_BODY, mBodyText.getText().toString());
                        if (mRowId != null) {
                            bundle.putLong(BillDbAdapter.KEY_ROWID, mRowId);
                        }
                        Intent mIntent = new Intent();
                        mIntent.putExtras(bundle);
                        setResult(RESULT_OK, mIntent);
                        finish();
                    }

                }
        );

    }

}
