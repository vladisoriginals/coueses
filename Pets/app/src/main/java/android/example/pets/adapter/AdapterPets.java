package android.example.pets.adapter;

import android.database.Cursor;
import android.example.pets.R;
import android.example.pets.data.PetContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPets extends RecyclerView.Adapter<AdapterPets.PetsViewHolder> {

    private Cursor cursor;

    public AdapterPets(Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public PetsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pets,parent, false);
        return new PetsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetsViewHolder holder, int position) {

        holder.bind(cursor);
    }

    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        }
        return cursor.getCount();
    }


    class PetsViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView breed;

        public PetsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name_pet);
            breed = itemView.findViewById(R.id.tv_breed_pet);
        }

        void bind (Cursor cursor){

            int nameColumnIndex = cursor.getColumnIndex(PetContract.PetsEntry.COLUMN_NAME);
            int breedColumnIndex = cursor.getColumnIndex(PetContract.PetsEntry.COLUMN_BREED);

            name.setText(cursor.getString(nameColumnIndex));
            breed.setText(cursor.getString(breedColumnIndex));
        }
    }
}
