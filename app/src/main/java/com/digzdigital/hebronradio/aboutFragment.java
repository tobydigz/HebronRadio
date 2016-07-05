package com.digzdigital.hebronradio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class aboutFragment extends Fragment implements View.OnClickListener {

    public aboutFragment() {

    }


    @Override
    public void onCreate(Bundle a) {
        super.onCreate(a);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle a) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        TextView email = (TextView) view.findViewById(R.id.emailHeb);
        email.setOnClickListener(this);

        TextView instagram = (TextView) view.findViewById(R.id.instaHeb);
        instagram.setOnClickListener(this);

        TextView twitter = (TextView) view.findViewById(R.id.twitHeb);
        twitter.setOnClickListener(this);

        TextView dialler = (TextView) view.findViewById(R.id.dialHeb);
        dialler.setOnClickListener(this);

        TextView facebook = (TextView) view.findViewById(R.id.faceHeb);
        facebook.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        MainActivity digz = (MainActivity) getActivity();
        switch (v.getId()){
            case R.id.emailHeb:
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] recipients = {"cuhebronfm@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Hello Hebron");
                intent.setType("text/html");
                startActivity(Intent.createChooser(intent, "Send mail"));
                break;
            case R.id.instaHeb:
                digz.getInstagram();
                break;
            case R.id.twitHeb:
                digz.getTwitter();
                break;
            case R.id.faceHeb:
                digz.getFacebook();
                break;
            case R.id.dialHeb:
                digz.call();
                break;
        }
    }
}
