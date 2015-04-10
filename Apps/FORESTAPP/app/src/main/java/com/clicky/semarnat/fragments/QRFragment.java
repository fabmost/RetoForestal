package com.clicky.semarnat.fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.clicky.semarnat.R;
import com.clicky.semarnat.utils.Contents;
import com.clicky.semarnat.utils.QRCodeEncoder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

/**
 *
 * Created by Clicky on 4/4/15.
 *
 */
public class QRFragment extends Fragment{

    private static String EXTRA_FOLIO = "folio";

    private ImageView imgQR;

    private String folio;

    public static QRFragment newInstance(String folio){
        QRFragment frag = new QRFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_FOLIO, folio);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_transportista_qr, container, false);

        imgQR = (ImageView)v.findViewById(R.id.img_qr);

        generateQR();

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decodeArguments();
    }

    private void decodeArguments(){
        Bundle args = getArguments();
        if(args != null) {
            folio = args.getString(EXTRA_FOLIO);
        }
    }

    private void generateQR(){
        //Find screen size
        Resources resources = getActivity().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = 250 * (metrics.densityDpi / 160f);

        //Encode with a QR Code image
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(folio,
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                (int)px);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();

            imgQR.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

}
