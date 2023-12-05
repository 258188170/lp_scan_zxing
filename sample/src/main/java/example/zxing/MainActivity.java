package example.zxing;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.openssl.crypto.ICAOCryptoJni;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    public static byte[] publickey = new byte[]{66, 69, 120, 106, 65, 54, 100, 53, 57, 84, 47, 52, 112, 79, 83, 83, 102, 57, 120, 105, 82, 112, 82, 67, 119, 86, 77, 55, 65, 106, 110, 67, 86, 47, 120, 53, 72, 48, 57, 111, 81, 69, 75, 106, 100, 122, 118, 104, 81, 76, 118, 75, 76, 79, 102, 111, 121, 80, 72, 69, 90, 67, 56, 104, 99, 105, 118, 116, 98, 107, 78, 83, 101, 52, 120, 53, 81, 50, 54, 73, 77, 119, 115, 68, 77, 73, 51, 61};

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Intent originalIntent = result.getOriginalIntent();
                    if (originalIntent == null) {
                        Log.d("MainActivity", "Cancelled scan");
                        Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                    } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                        Log.d("MainActivity", "Cancelled scan due to missing camera permission");
                        Toast.makeText(MainActivity.this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("MainActivity", "Scanned");
                    Toast.makeText(MainActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void scanBarcode(View view) {
        barcodeLauncher.launch(new ScanOptions().setOrientationLocked(true));
    }

    public void scanBarcodeInverted(View view) {
        ScanOptions options = new ScanOptions();
        options.addExtra(Intents.Scan.SCAN_TYPE, Intents.Scan.INVERTED_SCAN);
        barcodeLauncher.launch(options);
    }

    public void scanMixedBarcodes(View view) {
        ScanOptions options = new ScanOptions();
        options.addExtra(Intents.Scan.SCAN_TYPE, Intents.Scan.MIXED_SCAN);
        barcodeLauncher.launch(options);
    }

    public void scanBarcodeCustomLayout(View view) {
        ScanOptions options = new ScanOptions();
        options.setCaptureActivity(AnyOrientationCaptureActivity.class);
        options.setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES);
        options.setPrompt("Scan something");
        options.setOrientationLocked(true);
        options.setBeepEnabled(false);
        barcodeLauncher.launch(options);
    }

    public void scanPDF417(View view) {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan something");
        options.setOrientationLocked(false);
        options.setBeepEnabled(false);
        barcodeLauncher.launch(options);
    }


    public void scanBarcodeFrontCamera(View view) {
        ScanOptions options = new ScanOptions();
        options.setCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);
        barcodeLauncher.launch(options);
    }

    public void scanContinuous(View view) {
        Intent intent = new Intent(this, ContinuousCaptureActivity.class);
        startActivity(intent);
    }

    public void scanToolbar(View view) {
        ScanOptions options = new ScanOptions().setCaptureActivity(ToolbarCaptureActivity.class);
        barcodeLauncher.launch(options);
    }

    public void scanCustomScanner(View view) {
        ScanOptions options = new ScanOptions()
                .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                .addExtra(Intents.Scan.WIDTH, 300)
                .addExtra(Intents.Scan.HEIGHT, 300)
                .setOrientationLocked(true)
                .setCaptureActivity(CustomScannerActivity.class);
        barcodeLauncher.launch(options);
    }

    public void scanMarginScanner(View view) {
        ScanOptions options = new ScanOptions();
        options.setOrientationLocked(false);
        options.setCaptureActivity(SmallCaptureActivity.class);
        barcodeLauncher.launch(options);
    }

    public void scanWithTimeout(View view) {
        ScanOptions options = new ScanOptions();
        options.setTimeout(8000);
        barcodeLauncher.launch(options);
    }

    public void tabs(View view) {
        Intent intent = new Intent(this, TabbedScanning.class);
        startActivity(intent);
    }

    public void about(View view) {
        new LibsBuilder().start(this);
    }

    /**
     * Sample of scanning from a Fragment
     */
    public static class ScanFragment extends Fragment {
        private final ActivityResultLauncher<ScanOptions> fragmentLauncher = registerForActivityResult(new ScanContract(),
                result -> {
                    if (result.getContents() == null) {
                        Toast.makeText(getContext(), "Cancelled from fragment", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Scanned from fragment: " + result.getContents(), Toast.LENGTH_LONG).show();
                    }
                });

        public ScanFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_scan, container, false);
            Button scan = view.findViewById(R.id.scan_from_fragment);
            scan.setOnClickListener(v -> scanFromFragment());
            return view;
        }

        public void scanFromFragment() {
            fragmentLauncher.launch(new ScanOptions());
        }
    }
}
