package pl.softtech.personaladdressbook;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.softtech.personaladdressbook.data.DatabaseDescription;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /* Metody wywołania zwrotnego implementowane przez klasę MainActivity */
    public interface ContactFragmentListener {

        /* Wywołanie w wyniku wybrania kontaktu */
        void onContactSelected (Uri contactUri);

        /* Wywołanie w wyniku dotknięcia przycisku (+) */
        void onAddContact();
    }

    /* Identyfikator obiektu Loader */
    private static final int CONTACTS_LOADER = 0;

    /* Obiekt informujący aktywność MainActivity o wybraniu kontaktu */
    private ContactFragmentListener listener;

    /* Adapter obiektu RecyclerView */
    private ContactsAdapter contactAdapter;



    public ContactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);

        /* Przygotowanie do wyświetlenia graficznego interfejsu użytkownika */
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        /* Uzyskanie odwołania do widoku RecyclerView */
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        /* Konfiguracja widoku RecyclerView - widok powinien wyświetlać elementy w formie pionowej listy */
        recyclerView.setLayoutManager(new LinearLayoutManager((getActivity().getBaseContext())));

        contactAdapter = new ContactsAdapter(new ContactsAdapter.ContactClickListener() {
            @Override
            public void onClick (Uri contactUri) {
                listener.onContactSelected(contactUri);
            }
        });

        /* Ustawienie adaptera widoku RecyclerView */
        recyclerView.setAdapter(contactAdapter);

        /* Dołączenie spersonalizowanego obiektu ItemDivider */
        recyclerView.addItemDecoration(new ItemDivider(getContext()));

        /* Rozmiar widoku RecyclerVIew nie ulega zmnianie */
        recyclerView.setHasFixedSize(true);

        /* Inicjalizacja i konfiguracja przycisku dodawania kontaktu (+) */
        FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                listener.onAddContact();
            }
        });

        /* Zwrócenie widoku graficznego interfejsu użytkownika */
        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        listener = (ContactFragmentListener) context;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        listener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(CONTACTS_LOADER, null, this);
    }

    public void updateContactList(){
        contactAdapter.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {

        /* Utworzenie obiektu CursorLoader */
        switch(id){
            case CONTACTS_LOADER:
                return new CursorLoader(getActivity(),
                        DatabaseDescription.Contact.CONTENT_URI, // Adres URI tabeli kontaktów.
                        null, // Wartość null zwraca wszystkie kolumny.
                        null, // Wartość null zwraca wszystkie wiersze.
                        null, // Brak argumentów selekcji.
                        DatabaseDescription.Contact.COLUMN_NAME + " COLLATE NOCASE ASC"); // Kolejność sortowanie naszej tabeli
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        contactAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        contactAdapter.swapCursor(null);
    }
}
























