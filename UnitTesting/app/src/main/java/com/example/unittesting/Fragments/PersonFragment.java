package com.example.unittesting.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.example.unittesting.R;
import com.example.unittesting.Util.PreferenceUtil;
import com.example.unittesting.Util.PersonFormatUtil;
import com.example.unittesting.Model.Person;

public class PersonFragment extends Fragment
{
    private EditText editFirstName, editLastName, editAge;
    private TextView textPersonDisplay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        editFirstName = view.findViewById(R.id.edit_first_name);
        editLastName = view.findViewById(R.id.edit_last_name);
        editAge = view.findViewById(R.id.edit_age);
        textPersonDisplay = view.findViewById(R.id.text_person_display);
        Button btnSave = view.findViewById(R.id.btn_save);

        btnSave.setOnClickListener(v -> savePerson());

        displayPerson();

        return view;
    }

    private void savePerson()
    {
        try
        {
            String firstName = editFirstName.getText().toString();
            String lastName = editLastName.getText().toString();
            int age = Integer.parseInt(editAge.getText().toString());
            Person person = new Person(firstName, lastName, age);
            PreferenceUtil.saveFormatStyle(requireContext(), "custom");
            displayPerson();
            Toast.makeText(requireContext(), "Person saved", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(requireContext(), "Error saving person", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayPerson()
    {
        Person person = new Person("John", "Doe", 30);
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