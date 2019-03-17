package pl.wsb.bb.localizacja;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/*
* Aplikacja była testowana na API Oreo 26

Funkcjonować powinno od wersji API 23 czyli Marshmellow

Ze StackOverflow zaczerpnąłem metodę do przeliczania Decymali na Stopnie oraz wyliczania odległości - która chyba nieźle działa.

Oraz częściowo nadpisałem własne metody sprawdzające permission poszukując błedu (były bardzo podobne)
Okazało się, że chodziło o Try Catch...
GPS po ponownym włączeniu ZANIM poda dane powodował NullPointerException i try-catch okazało się rozwiązaniem.

* */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClickActivity(View view) {
            Intent intent1 = new Intent(this, LocaliseActivity.class);
            startActivity(intent1);

        }

}
