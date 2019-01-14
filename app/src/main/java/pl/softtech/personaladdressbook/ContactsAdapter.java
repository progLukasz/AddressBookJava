package pl.softtech.personaladdressbook;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.softtech.personaladdressbook.data.DatabaseDescription;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    /* Definicja interfejsu implementowanego przez klasę ContactFragment */
    public interface ContactClickListener{
        void onClick(Uri contactUri);
    }

    /* Klasa używana do implementacji wzorca ViewHolder w kontekscie widoku RecyclerView */
    public class ViewHolder extends RecyclerView.ViewHolder{

        /* Widok TextView wyświetlający nazwę kontaktu */
        public final TextView textView;

        /* Identyfikator rzędu kontaktu */
        private long rowID;

        /* Konstruktor klasy ViewHolder */
        public ViewHolder(View view){
            super(view);

            /* Inicjalizacja widoku TextView */
            textView = (TextView) view.findViewById(R.id.text);

            /* Podłącz do obiektu View obiekt nasłuchujący zdarzeń */
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    clickListener.onClick(DatabaseDescription.Contact.buildContactUri(rowID));
                }
            });
        }

        /* Określenie identyfikatora rzędu */
        public void setRowID(long rowID){
            this.rowID = rowID;
        }
    }

    /* Zmienne egzemplarzowe */
    private Cursor cursor = null;
    private final ContactClickListener clickListener;

    /* Konstruktor klasy ContactsAdapter */
    public ContactsAdapter(ContactClickListener clickListener){
        this.clickListener = clickListener;
    }

    /* Uzyskanie obiektu ViewHolder bierzącego elementu kontaktu */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype){

        /* Przygotowanie do wyświetlenia predefiniowanego rozkładu Androida */
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        /* Zwrócenie obiektu ViewHolder bieżącego elementu */
        return new ViewHolder(view);
    }

    /* Określenie tekstu elementu listy */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        /* Przeniesienie wybranego kontaktu w odpiwednie miejsce widoku RecyclerView */
        cursor.moveToPosition(position);

        /* Określenie identyfikatora rowID elementu ViewHolder */
        holder.setRowID(cursor.getLong(cursor.getColumnIndex(DatabaseDescription.Contact._ID)));

        /* Ustawienie tekstu widoku TextView elementu widoku RecyclerView */
        holder.textView.setText(cursor.getString(cursor.getColumnIndex(DatabaseDescription.Contact.COLUMN_NAME)));
    }


    /* Zwraca liczbę elementów wiązanych przez adapter */
    @Override
    public int getItemCount(){
        return (cursor != null) ? cursor.getCount() : 0;
    }

    /* Zamienia bierzący obiekt Cursor na nowy */
    public void swapCursor(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }
}
