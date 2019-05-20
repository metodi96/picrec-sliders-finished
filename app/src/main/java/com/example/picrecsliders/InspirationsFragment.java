package com.example.picrecsliders;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.tooltip.Tooltip;
import java.util.HashMap;
import java.util.Map;

public class InspirationsFragment extends Fragment {
    private Tooltip tooltip;
    private HashMap<String, Integer> rolesToPoints = new HashMap<String, Integer>() {{
        put("active1", 50);
        put("drifter1", 50);
        put("escapist1", 50);
        put("sun2",50);
        put("sun1",50);
        put("arch1",50);
        put("arch2",50);
        put("classy1",50);
        put("drifter2", 50);
    }};
    ProfileFragment profileFragment = MainActivity.profileFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inspirations, container, false);
            GridLayout gridLayout = (GridLayout) v.findViewById(R.id.fragment_inspirations_layout);
            for (String role : rolesToPoints.keySet()) {
                addInspiration(role, gridLayout);
            }
        return v;
    }


    private void addInspiration(final String role, GridLayout layout) {
        //add a new button
        View item = getLayoutInflater().inflate(R.layout.pictures_inspirations, null);
        int roleId = getResources().getIdentifier(role, "drawable", getActivity().getPackageName());
        ImageView iw = item.findViewById(R.id.picture_id);
        iw.setImageResource(roleId);
        iw.setClipToOutline(true);

        SeekBar seekBar = item.findViewById(R.id.seekbar);
        layout.addView(item);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (progress < 35) {
                        seekBar.setThumb((ContextCompat.getDrawable(getContext(), R.drawable.sad)));
                        seekBar.getProgressDrawable().setColorFilter(Color.parseColor("#F44336"), PorterDuff.Mode.SRC_IN);
                    }
                    else if (progress > 65) {
                        seekBar.setThumb((ContextCompat.getDrawable(getContext(), R.drawable.happy)));
                        seekBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                    }
                    else {
                        seekBar.setThumb((ContextCompat.getDrawable(getContext(), R.drawable.confused)));
                        seekBar.getProgressDrawable().setColorFilter(Color.parseColor("#EEDA17"), PorterDuff.Mode.SRC_IN);

                    }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                rolesToPoints.put(role, seekBar.getProgress());
                savePoints();
                resetPoints();
            }
        });
    }

    public void resetPoints() {
        Button resetPoints = getView().findViewById(R.id.reset);
        resetPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   for (Map.Entry<String, Integer> entry : rolesToPoints.entrySet()) {
                        String key = entry.getKey();
                        rolesToPoints.put(key, 50);
                    }
                    InspirationsFragment fragment = (InspirationsFragment)
                            getFragmentManager().findFragmentById(R.id.fragment_container);

                    getFragmentManager().beginTransaction()
                            .detach(fragment)
                            .attach(fragment)
                            .commit();
                }
            });
    }

    public void savePoints() {
        final Button savePoints = getView().findViewById(R.id.save_preferences);
       savePoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countNotManipulatedFields(rolesToPoints) > 3) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("rolesToPoints",rolesToPoints);
                    profileFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();

                } else {
                    tooltip = new Tooltip.Builder(savePoints)
                            .setText("Prioritize at least 4 pictures")
                            .setCancelable(true)
                            .setGravity(Gravity.START)
                            .setCornerRadius(25f)
                            .setTextColor(Color.WHITE)
                            .setTextStyle(R.font.unicodearialr)
                            .setTextSize(15f)
                            .setPadding(20)
                            .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                            .show();
                }
            }
        });
    }


    public int totalPoints(HashMap<String, Integer> hashMap) {
        int result = 0;
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            Integer value = entry.getValue();
            result+=value;
        }
        return result;
    }

    public int countNotManipulatedFields(HashMap<String, Integer> hashMap) {
        int count = 0;
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            Integer value = entry.getValue();
            if (value != 50) {
                count += 1;
            }
        }
        return count;
    }


}
