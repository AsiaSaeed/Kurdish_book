package com.example.myapplication;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Model.Wanekan;

public class SplitActivity extends AppCompatActivity implements View.OnDragListener, View.OnLongClickListener {

    private TextView textView1, textView2;
    MediaPlayer mediaPlayer;
    boolean state = false;
    Button next;
    Button back;
    int scoreSplitActivity;
    int generalScore;
    private static final String TEXT_VIEW_TAG = "DRAG TEXT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);
        findViews();
        implementEvents();
        MediaPlayer jyakrdnawaRutalkrdn=MediaPlayer.create(this,R.raw.jyakrdnawa);
        jyakrdnawaRutalkrdn.start();
        next = findViewById(R.id.next);
        back=findViewById(R.id.back_sa);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SplitActivity.this, WanekanActivity.class);
                startActivity(intent);
            }
        });
        final Intent intent1 = getIntent();
        generalScore = intent1.getIntExtra("scoreRahenaniDw", 0);


        next.setEnabled(false);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SplitActivity.this,  "پیرۆزە کۆی گشتی: " +generalScore+" نمرە", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SplitActivity.this, CircleTheWordActivity.class);
                intent.putExtra("SplitActivityScore",generalScore);
                startActivity(intent);
            }
        });


    }

    public void playSoundFile(Integer fileName) {
        mediaPlayer = MediaPlayer.create(this, fileName);
        mediaPlayer.start();
    }

    public void jyakrdnawa(View v) {
        playSoundFile(R.raw.jyakrdnawa);
    }

    public void amad(View v) {
        playSoundFile(R.raw.amad_ama_d);
    }

    //Find all views and set Tag to all draggable views
    private void findViews() {
        textView1 = (TextView) findViewById(R.id.label1);
        textView1.setTag(TEXT_VIEW_TAG);
        textView2 = (TextView) findViewById(R.id.label2);
        textView2.setTag(TEXT_VIEW_TAG);

    }


    //Implement long click and drag listener
    private void implementEvents() {
        //add or remove any view that you don't want to be dragged
        textView1.setOnLongClickListener(this);
        textView2.setOnLongClickListener(this);

        //add or remove any layout view that you don't want to accept dragged view
        findViewById(R.id.top_layout).setOnDragListener(this);
        findViewById(R.id.left_layout).setOnDragListener(this);
        findViewById(R.id.right_layout).setOnDragListener(this);
    }

    @Override
    public boolean onLongClick(View view) {
        // Create a new ClipData.
        // This is done in two steps to provide clarity. The convenience method
        // ClipData.newPlainText() can create a plain text ClipData in one step.

        // Create a new ClipData.Item from the ImageView object's tag
        ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

        // Create a new ClipData using the tag as a label, the plain text MIME type, and
        // the already-created item. This will create a new ClipDescription object within the
        // ClipData, and set its MIME type entry to "text/plain"
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

        ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);

        // Instantiates the drag shadow builder.
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

        // Starts the drag
        view.startDrag(data//data to be dragged
                , shadowBuilder //drag shadow
                , view//local data about the drag and drop operation
                , 0//no needed flags
        );

        //Set view visibility to INVISIBLE as we are going to drag the view
        view.setVisibility(View.INVISIBLE);
        return true;
    }

    // This is the method that the system calls when it dispatches a drag event to the
    // listener.
    @Override
    public boolean onDrag(View view, DragEvent event) {
        // Defines a variable to store the action type for the incoming event
        int action = event.getAction();
        // Handles each of the expected events
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                // Determines if this View can accept the dragged data
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    // if you want to apply color when drag started to your view you can uncomment below lines
                    // to give any color tint to the View to indicate that it can accept
                    // data.

                    //view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);//set background color to your view

                    // Invalidate the view to force a redraw in the new tint
                    //  view.invalidate();

                    // returns true to indicate that the View can accept the dragged data.
                    return true;

                }

                // Returns false. During the current drag and drop operation, this View will
                // not receive events again until ACTION_DRAG_ENDED is sent.
                return false;


            case DragEvent.ACTION_DRAG_LOCATION:
                // Ignore the event
                return true;

            case DragEvent.ACTION_DROP:
                // Gets the item containing the dragged data
                ClipData.Item item = event.getClipData().getItemAt(0);

                // Gets the text data from the item.
                String dragData = item.getText().toString();

                // Displays a message containing the dragged data.
                Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();

                // Turns off any color tints


                // Invalidates the view to force a redraw
                view.invalidate();

                View v = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) v.getParent();
                owner.removeView(v);//remove the dragged view
                LinearLayout container = (LinearLayout) view;//caste the view into LinearLayout as our drag acceptable layout is LinearLayout
                container.addView(v);//Add the dragged view
                v.setVisibility(View.VISIBLE);//finally set Visibility to VISIBLE

                // Returns true. DragEvent.getResult() will return true.
                return true;
            case DragEvent.ACTION_DRAG_ENDED:


                // Does a getResult(), and displays what happened.
                if (event.getResult()) {
                    Toast.makeText(this, "دانانەکەت سەرکەوتو بوو +1نمرە ", Toast.LENGTH_SHORT).show();
                    scoreSplitActivity++;
                    if (scoreSplitActivity == 2) {
                        generalScore = generalScore + scoreSplitActivity;
                        next.setEnabled(true);
                    }
                } else
                    Toast.makeText(this, "دانانەکەت هەلە بو دوبارە کەوە", Toast.LENGTH_SHORT).show();


                // returns true; the value is ignored.
                return true;

            // An unknown action type was received.
            default:
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }


}