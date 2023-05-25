package t3h.android.elifeadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import java.util.Objects;

import t3h.android.elifeadmin.listener.OnBackPressedListener;
import t3h.android.elifeadmin.ui.DashboardFragment;
import t3h.android.elifeadmin.ui.SignInFragment;

public class MainActivity extends AppCompatActivity implements OnBackPressedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onFragmentBackPressed() {
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        Fragment navHostFragment = Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.navHostFragment));
        Fragment currentFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
        if (currentFragment instanceof DashboardFragment || currentFragment instanceof SignInFragment) {
            ((OnBackPressedListener) currentFragment).onFragmentBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}