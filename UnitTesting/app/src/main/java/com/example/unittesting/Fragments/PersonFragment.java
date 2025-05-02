package com.example.unittesting.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.unittesting.Model.Person;
import com.example.unittesting.R;
import com.example.unittesting.Util.PersonFormatUtil;
import com.example.unittesting.Util.PreferenceUtil;

// Brenna Pavlinchak
// AD3 - C202504
// PersonFragment

public class PersonFragment extends Fragment
{
    private EditText editFirstName, editLastName, editAge;
    private TextView textPersonDisplay;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        editFirstName = view.findViewById(R.id.edit_first_name);
        editLastName = view.findViewById(R.id.edit_last_name);
        editAge = view.findViewById(R.id.edit_age);
        textPersonDisplay = view.findViewById(R.id.text_person_display);

        displayPerson();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_person, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.action_save)
        {
            savePerson();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void savePerson()
    {
        try {
            String firstName = editFirstName.getText().toString();
            String lastName = editLastName.getText().toString();
            int age = Integer.parseInt(editAge.getText().toString());
            Person person = new Person(firstName, lastName, age);
            PreferenceUtil.savePerson(requireContext(), person);
            PreferenceUtil.saveFormatStyle(requireContext(), "custom");
            displayPerson();
            Toast.makeText(requireContext(), "Person saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error saving person", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayPerson()
    {
        Person person = PreferenceUtil.getPerson(requireContext());

        if (person == null)
        {
            person = new Person("John", "Doe", 30);
        }

        String formatStyle = PreferenceUtil.getFormatStyle(requireContext());

        if ("custom".equals(formatStyle))
        {
            textPersonDisplay.setText(PersonFormatUtil.formatFullName(person));
        }
        else
        {
            textPersonDisplay.setText(PersonFormatUtil.formatPersonSummary(person));
        }
    }
}