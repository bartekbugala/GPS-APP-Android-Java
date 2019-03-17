package pl.wsb.bb.localizacja;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/* Aktywność zawierająca lokalizację */
public class LocaliseActivity extends AppCompatActivity implements LocationListener {

    private String provider;
    private LocationManager lm;
    private double mojaSzerokosc;
    private double mojaDlugosc;
    private double mojaSzerokoscSave = 200; // Współrzędne nieistniejące do kontroli czy jakieś zostały zapisane
    private double mojaDlugoscSave = 200;
    final int LOCATION_PERMISSION_REQUEST_CODE = 1252;
    Criteria defaultCriteria = new Criteria();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localise);
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        provider = lm.getBestProvider(defaultCriteria, false);

        lokalizatorGPS(); // Wrzucone do metody - w celu ewentualnej modyfikacji activity wykorzystania w innym miejscu kodu

    }

    /* Wymagana Implementacja interfejsu LocationListener */
    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    /* Dzięki zastosowaniu metody mogę wykorzystywać lokalizator w buttonie */
    public void lokalizatorGPS() {
        /* Try-Catch rozwiązało mój główny problem i zupełnie niepotrzebnie posiłkowałem się sprawdzaniem permission ze Stackoverflow
          Moje metody spradzania wersji były znacznie prostsze okazuje się, że błąd polegał głównie na wyrzucaniu NullPointEcveption co wychwyciłem w debuggerze */
        try {
            boolean isEnabledGPS = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isEnabledGPS == true) {
                /* Wygenerowane przez Android Studio - wypełnione przeze mnie*/
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Toast.makeText(this, "Włącz lokalizację GPS dla urządzenia i tej aplikacji", Toast.LENGTH_LONG).show();
                    if (Build.VERSION.SDK_INT >= 23) { // Marshmallow

                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                    }

                    return;
                }
                Location location = lm.getLastKnownLocation(provider);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Toast.makeText(this, "Włącz lokalizację GPS dla urządzenia i tej aplikacji", Toast.LENGTH_LONG).show();
                    if (Build.VERSION.SDK_INT >= 23) { // Marshmallow

                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                    }
                    else {
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                    }

                    return;
                }
                //
                lm.requestLocationUpdates(provider, 6000, (float) 0.1, this);
                mojaSzerokosc = (double) (location.getLatitude());
                mojaDlugosc = (double) (location.getLongitude());

                TextView szerokoscGeo = (TextView) findViewById(R.id.szerokoscGeo);
                szerokoscGeo.setText("Szerokość: " + mojaSzerokosc);

                TextView dlugoscGeo = (TextView) findViewById(R.id.dlugoscGeo);
                dlugoscGeo.setText("Długość: " + mojaDlugosc);

                TextView wspolrzedneStopnie = (TextView) findViewById(R.id.wspolrzedneStopnie);
                wspolrzedneStopnie.setText("Współrzędne: " + getFormattedLocationInDegree(mojaSzerokosc, mojaDlugosc));
            } else {
                TextView szerokoscGeo = (TextView) findViewById(R.id.szerokoscGeo);
                szerokoscGeo.setText("Szerokość: BRAK");

                TextView dlugoscGeo = (TextView) findViewById(R.id.dlugoscGeo);
                dlugoscGeo.setText("Długość: BRAK");

                TextView wspolrzedneStopnie = (TextView) findViewById(R.id.wspolrzedneStopnie);
                wspolrzedneStopnie.setText("Współrzędne: BRAK");
                Toast.makeText(this, "Włącz lokalizację GPS dla urządzenia i tej aplikacji", Toast.LENGTH_LONG).show();
            }

        } catch (NullPointerException e) {
            return;

        }

    }

    /* Metoda ze Stackoverflow https://stackoverflow.com/questions/21382230/how-to-format-gps-latitude-and-longitude */
    public static String getFormattedLocationInDegree(double latitude, double longitude) {
        try {
            int latSeconds = (int) Math.round(latitude * 3600);
            int latDegrees = latSeconds / 3600;
            latSeconds = Math.abs(latSeconds % 3600);
            int latMinutes = latSeconds / 60;
            latSeconds %= 60;

            int longSeconds = (int) Math.round(longitude * 3600);
            int longDegrees = longSeconds / 3600;
            longSeconds = Math.abs(longSeconds % 3600);
            int longMinutes = longSeconds / 60;
            longSeconds %= 60;
            String latDegree = latDegrees >= 0 ? "N" : "S";
            String lonDegrees = longDegrees >= 0 ? "E" : "W";

            return Math.abs(latDegrees) + "°" + latMinutes + "'" + latSeconds
                    + "\"" + latDegree + " " + Math.abs(longDegrees) + "°" + longMinutes
                    + "'" + longSeconds + "\"" + lonDegrees;
        } catch (Exception e) {
            return " " + String.format("%8.5f", latitude) + "  "
                    + String.format("%8.5f", longitude);
        }
    }


    /* Zapisanie aktualnej pozycji do obliczania odległości*/
    public void onClickSav(View view) {
        mojaSzerokoscSave = mojaSzerokosc;
        mojaDlugoscSave = mojaDlugosc;

        TextView szerokoscGeoSav = (TextView) findViewById(R.id.szerokoscGeoSav);
        szerokoscGeoSav.setText("Szerokość: " + mojaSzerokoscSave);

        TextView dlugoscGeoSav = (TextView) findViewById(R.id.dlugoscGeoSav);
        dlugoscGeoSav.setText("Długość: " + mojaDlugoscSave);

        TextView wspolrzedneStopnieSav = (TextView) findViewById(R.id.wspolrzedneStopnieSav);
        wspolrzedneStopnieSav.setText("Współrzędne: " + getFormattedLocationInDegree(mojaSzerokoscSave, mojaDlugoscSave));
    }

    /* Funkcja odświeżająca lokalizację i obliczająca odległość między zapisanymi współrzędnymi, a aktualnymi */
    public void onClickLok(View view) {

        lokalizatorGPS();

        if (mojaDlugoscSave != 200 && mojaSzerokoscSave != 200) {
            TextView odleglosc = (TextView) findViewById(R.id.odleglosc);
            int odlegloscx = (int) distance(mojaSzerokosc, mojaSzerokoscSave, mojaDlugosc, mojaDlugoscSave, 0.0, 0.0);
            odleglosc.setText("Odległość: " + odlegloscx + " m / " + odlegloscx / 1000 + " km");
        }

    }

    /**
     * ZACZERPNIĘTE ZE STACKOVERFLOW
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     * <p>
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     *
     * @returns Distance in Meters
     */
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // start to find location...
                return;

            } else { // if permission is not granted

                // decide what you want to do if you don't get permissions
                Toast.makeText(this, "Włącz lokalizację GPS dla urządzenia i tej aplikacji", Toast.LENGTH_LONG).show();
            }
        }
    }

}