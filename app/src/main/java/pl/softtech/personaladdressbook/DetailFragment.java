package pl.softtech.personaladdressbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.softtech.personaladdressbook.data.DatabaseDescription;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /* Interfejs z metodą wywołania zwrotnego implementowaną przez główną aktywność */
    public interface DetailFragmentListener {

        /* Metda wywołana w przypadku usuwania kontaktu */
        void onContactDeleted();

        /* Przekazanie adresu URI, który ma być edytowany */
        void onEditContact(Uri contactUri);
    }

    /* Pole używane do identyfikacji obiektu Loader */
    private static final int CONTACT_LOADER = 0;

    /* Pole obiektu implementującego zagnieżdżony interfejs - główna aktywność (MainActivity) */
    private DetailFragmentListener listener;

    /* Adres URI wybranego kontaktu */
    private Uri contactUri;

    /* Pola widoków TextView */
    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView emailTextView;
    private TextView streetTextView;
    private TextView cityTextView;
    private TextView stateTextView;
    private TextView zipTextView;

    /* Inicjalizacja interfejsu DetailFragmentListener przy dołączeniu fragmentów */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DetailFragmentListener) context;
    }

    /* Usuwanie interfejsu DetailFragmentListener przy odłączeniu fragmentu */
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    /* Utworzenie widoku obiektu fragment */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);

        /* Uzyskanie obiektu Bundle zawierającego argumenty */
        Bundle arguments = getArguments();

        /* Odczytanie z argumentów adresu URI */
        if (arguments != null) {
            contactUri = arguments.getParcelable(MainActivity.CONTACT_URI);
        }

        /* Przygotowanie do wyświetlenia rozkładu fragmentu DetailFragment */
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        /* Inicjalizacja pól TextView */
        nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        phoneTextView = (TextView) view.findViewById(R.id.phoneTextView);
        emailTextView = (TextView) view.findViewById(R.id.emailTextView);
        streetTextView = (TextView) view.findViewById(R.id.streetTextView);
        cityTextView = (TextView) view.findViewById(R.id.cityTextView);
        stateTextView = (TextView) view.findViewById(R.id.stateTextView);
        zipTextView = (TextView) view.findViewById(R.id.zipTextView);

        /* Załadowanie kontaktu */
        getLoaderManager().initLoader(CONTACT_LOADER, null, this);

        /* Zwrócenie widoku */
        return view;
    }

    /* Wyświetla elementy menu fragmenu DetailFragment */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_details_menu, menu);
    }

    /* Obsługa wybranego elementu menu */
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                listener.onEditContact(contactUri);
                return true;
            case R.id.action_delete:
                deleteContact();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Usunięcie kontaktu */
    private void deleteContact() {
        
        /* Utworzenie obiektu AlertDialog potweirdzającego usunięcie wybranego kontaktu */
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.confirm_title);
        builder.setMessage(R.string.confirm_message);
        builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getActivity().getContentResolver().delete(contactUri, null, null);
                listener.onContactDeleted();
            }
        });
        builder.setNegativeButton(R.string.button_cancel, null);

        /* Wyświetlanie zdefiniowanego okna dialogowego */
        builder.show();
    }


    /* Wywołanie w celu utowrzenia obiektu Loader */
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {

        /* Definicja obiektu CursorLoader */
        CursorLoader cursorLoader;

        switch(id) {
            case CONTACT_LOADER:
                cursorLoader = new CursorLoader(getActivity(), contactUri, null, null, null, null);
                break;
            default:
                cursorLoader = null;
                break;
        }
        return cursorLoader;
    }

    /* Wywołanie przez LoaderManager w celu zakończenia ładowania danych */
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        /* Wyświetlenie danych istniejącego kontaktu */
        if (data != null && data.moveToFirst()) {

            /* Odczytanie indeksów kolumn z tabeli */
            int nameIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_NAME);
            int phoneIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_PHONE);
            int emailIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_EMAIL);
            int streetIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_STREET);
            int cityIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_CITY);
            int stateIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_STATE);
            int zipIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_ZIP);

            /* Wypełnienie pól EditText */
            nameTextView.setText(data.getString(nameIndex));
            phoneTextView.setText(data.getString(phoneIndex));
            emailTextView.setText(data.getString(emailIndex));
            streetTextView.setText(data.getString(streetIndex));
            cityTextView.setText(data.getString(cityIndex));
            stateTextView.setText(data.getString(stateIndex));
            zipTextView.setText(data.getString(zipIndex));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
