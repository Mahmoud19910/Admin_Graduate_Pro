package com.sarmed.my_admin.FireBase_Service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sarmed.my_admin.Interface.GmailDataListener;


public class Gmai_Auth {
    public static int RC_SIGN_IN = 2;
    public static GoogleSignInClient mGoogleSignInClient;
    public static GoogleSignInOptions gso;
    public static FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    public static GmailDataListener gmailDataListener;

    //OnCreate تنفذ في
    public static void createGoogleAuth(Context context) {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    //تنفذ عند الضغط على زر تسجيل جوجل
    public static void onClickGoogleBut(Context context) {
        if (mGoogleSignInClient == null) {
            // Handle the null case, show an error message or log a warning.
            Toast.makeText(context, "Google Sign-In is not initialized", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }


    //onActivityResult تنفذ في
    public static void onResultGoolgleAuth(Context context, int requestCode, Intent data) {
        gmailDataListener = (GmailDataListener) context;
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Task Success", Toast.LENGTH_SHORT).show();
                    // Signed in successfully, show authenticated UI.
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    // Use account.getIdToken() to authenticate with your server.
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    String id = account.getId();
                    String email = account.getEmail();
                    String name = account.getDisplayName();
                    String photoUrl = null;

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        Uri userPhotoUri = currentUser.getPhotoUrl();
                        if (userPhotoUri != null) {
                            photoUrl = userPhotoUri.toString();
                        }
                    }

                    // Call the listener with the retrieved data
                    gmailDataListener.getGmailData(id, email, name, photoUrl);
                }
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("ApiException Exp: " + e.getMessage());
            } catch (Exception e) {
                // Handle any other exceptions
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("Exception Exp: " + e.getMessage());
            }
        }
    }



    // عند الضغط على زر تسجيل الخروج
    public static void onSignOut(Context context){
        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
        Toast.makeText(context, "Sign Out", Toast.LENGTH_SHORT).show();
    }

    // Loged In
//    public static void isLogIn(Context context){
//        if(firebaseAuth.getCurrentUser()!=null && context instanceof Activity){
//            ((Activity)context).startActivity(new Intent(context , Home_Activity.class));
//        }else{
//            ((Activity)context).startActivity(new Intent(context , UnBoarding_Activity.class));
//
//        }
//    }


}
