package t3h.android.elifeadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import t3h.android.elifeadmin.listener.OnBackPressedListener;

public class MainActivity extends AppCompatActivity implements OnBackPressedListener {
    private boolean backPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onFragmentBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.dashboardFragment);
        if (fragment instanceof OnBackPressedListener) {
            ((OnBackPressedListener) fragment).onFragmentBackPressed();
        } else {
            finishAffinity();
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedOnce) {
            onFragmentBackPressed();
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            backPressedOnce = true;
            new Handler().postDelayed(() -> backPressedOnce = false, 2000); // Reset the flag after a delay
        }
    }
}