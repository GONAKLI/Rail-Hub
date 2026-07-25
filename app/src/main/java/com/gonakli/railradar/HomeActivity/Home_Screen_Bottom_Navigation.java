package com.gonakli.railradar.HomeActivity;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gonakli.railradar.PNR_Work.PNR_Check;
import com.gonakli.railradar.R;

public class Home_Screen_Bottom_Navigation extends RelativeLayout {
    Context context;
    RelativeLayout pnr_Search_Container, book_Ticket_Container;
    public Home_Screen_Bottom_Navigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
       LayoutInflater.from(context).inflate(R.layout.bottom_navigation_tab, this, true);
       find_all_by_id();
       on_Click_Action();
    }

    private void on_Click_Action() {
        pnr_Search_Container.setOnClickListener(v ->{
            Intent pnrScreen = new Intent(context, PNR_Check.class);
            context.startActivity(pnrScreen);
        });

        book_Ticket_Container.setOnClickListener(v ->{
            Toast.makeText(context, "book clicked", Toast.LENGTH_SHORT).show();
        });
    }

    private void find_all_by_id() {
        pnr_Search_Container =  findViewById(R.id.pnr_search_container);
        book_Ticket_Container = findViewById(R.id.book_ticket_container);
    }


}
