package t3h.android.elifeadmin.helper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class FirebaseAuthHelper {
    private static final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public static void signIn(String email, String password, OnCompleteListener<AuthResult> listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    public static void forgotPassword(String email, OnCompleteListener<Void> listener) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(listener);
    }

    public static void changePassword(String oldPassword, String newPassword, OnCompleteListener<Void> listener) {
        FirebaseUser firebaseUser = getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(firebaseUser.getEmail()), oldPassword);
        firebaseUser.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                firebaseUser.updatePassword(newPassword).addOnCompleteListener(listener);
            } else {
                listener.onComplete(task);
            }
        });
    }

    public static void signOut() {
        firebaseAuth.signOut();
    }
}
