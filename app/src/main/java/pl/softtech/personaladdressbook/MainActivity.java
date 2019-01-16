package pl.softtech.personaladdressbook;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements ContactsFragment.ContactFragmentListener,
        DetailFragment.DetailFragmentListener, AddEditFragment.AddEditFragmentListener {

    /* Klucz przechowujący adres URI kontaktu w obiekcie przekazanym do fragmentu */
    public static final String CONTACT_URI = "contact_uri";

    /* Fragment wyświetlający listę kontaktów z bazy */
    private ContactsFragment contactsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Jeżeli rozkład głównej aktywności zawiera fragment FragmentContainer (content_main.xml),
         * to oznacza używanie rozkładu przeznaczonego dla telefonu. W takiej sytuacji tworzymy
         * i wyświetlamy fragment ContactsFragment */
        if (savedInstanceState == null && findViewById(R.id.fragmentContainer) != null) {

            /* Utworzenie fragmentu ContactsFragment */
            contactsFragment = new ContactsFragment();

            /* Dodanie fragmentu do rozkładu FrameLayout */
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentContainer, contactsFragment);

            /* Wyświetl obiekt ContactsFragment */
            transaction.commit();
        } else {

            /* Uzyskanie odwołania do już istniejącego fragmentu ContactsFragment */
            contactsFragment = (ContactsFragment) getSupportFragmentManager().findFragmentById(R.id.contactsFragment);
        }
    }

    /* Utworzenie i wyświetlenie fragmentu DetailFragment */
    private void displayDetailFragment(Uri contactUri, int viewID) {

        /* Utworzenie fragmentu DetailFragment */
        DetailFragment detailFragment = new DetailFragment();

        /* Przekazanie adresu URI jako argumentu fragmentu */
        Bundle arguments = new Bundle();
        arguments.putParcelable(CONTACT_URI, contactUri);
        detailFragment.setArguments(arguments);

        /* Dodanie fragmentu do rozkładu */
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, detailFragment);
        transaction.addToBackStack(null);

        /* Wyświetl obiekt DetailFragment */
        transaction.commit();
    }

    /* Utworzenie i wyświetlenie fragmentu AddEditFragment */
    private void displayAddEditFragment(Uri contactUri, int viewID) {

        /* Utowrzenie fragmentu AddEditFragment */
        AddEditFragment addEditFragment = new AddEditFragment();

        /* Jeżeli edytowany jest wczesniej zapisany kontakt, to jako argument przekazywany jest contactUri */
        if (contactUri != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(CONTACT_URI, contactUri);
            addEditFragment.setArguments(arguments);
        }

        /* Dodanie fragmentu do rozkładu */
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, addEditFragment);
        transaction.addToBackStack(null);

        /* Wyświetl obiekt AddEditFragment */
        transaction.commit();
    }


    @Override
    public void onAddEditCompleted(Uri contactUri) {

        /* Usunięcie górnego elementu stosu aplikacji */
        getSupportFragmentManager().popBackStack();

        /* Odświeżenie listy kontaktów */
        contactsFragment.updateContactList();

        /* Obsługa aplikacji na tablecie */
        if (findViewById(R.id.fragmentContainer) == null) {

            /* Usunięcie górnego elementu stosu aplikacji */
            getSupportFragmentManager().popBackStack();

            /* Wyśwetlenie kontaktu, który zotał dodany lub zaktualizowany */
            displayDetailFragment(contactUri, R.id.rightPaneContainer);
        }
    }

    @Override
    public void onContactSelected(Uri contactUri) {

        /* Wyświetlenie fragmentu DetailFragment dla wybranego kontaktu na telefonie... */
        if (findViewById(R.id.fragmentContainer) != null) {
            displayDetailFragment(contactUri, R.id.fragmentContainer);
        }

        /* ... na tablecie */
        else {
            getSupportFragmentManager().popBackStack();
            displayDetailFragment(contactUri, R.id.rightPaneContainer);
        }
    }

    @Override
    public void onAddContact() {

        /* Wyświetlenie fragmentu AddEditFragment dla wybranego kontaktu na telefonie... */
        if (findViewById(R.id.fragmentContainer) != null) {
            displayAddEditFragment(null, R.id.fragmentContainer);
        }

        /* ... na tablecie */
        else {
            displayAddEditFragment(null, R.id.rightPaneContainer);
        }

    }

    @Override
    public void onContactDeleted() {

        /* Usunięcie elementu znajdującego się na szczycie stoku aplikacji */
        getSupportFragmentManager().popBackStack();

        /* Odświerzenie listy kontaktów fragmentu ContactsFragment */
        contactsFragment.updateContactList();
    }

    @Override
    public void onEditContact(Uri contactUri) {

        /* Wyświetlenie fragmentu AddEditFragment dla wybranego kontaktu na telefonie... */
        if (findViewById(R.id.fragmentContainer) != null) {
            displayAddEditFragment(contactUri, R.id.fragmentContainer);
        }

        /* ... na tablecie */
        else {
            displayAddEditFragment(contactUri, R.id.rightPaneContainer);
        }
    }
}
