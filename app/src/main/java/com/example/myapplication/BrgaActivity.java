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

public class BrgaActivity extends AppCompatActivity implements View.OnDragListener, View.OnLongClickListener {

    public int score = 0;
    boolean incremented = true;
    private static final String TAG = BrgaActivity.class.getSimpleName();
    private TextView textView1, textView2, textView3, textView4;
    MediaPlayer mediaPlayer;

    public static final String EXTRA_MESSAGE = "com.example.lenovo.drapdrop";

    private static final String TEXT_VIEW_TAG = "DRAG TEXT";

    /* public void sendMessage(View view) {
         Intent intent = new Intent(this, sec.class);

         String message = "done";
         intent.putExtra(EXTRA_MESSAGE, message);
         if(score==4) {
             startActivity(intent);
         }
     }
 */
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brga);
        findViews();
        implementEvents();
        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrgaActivity.this, Toxbkawa1Activity.class);
                startActivity(intent);
            }
        });

    }

    public void playSoundFile(Integer fileName) {
        mediaPlayer = MediaPlayer.create(this, fileName);
        mediaPlayer.start();
    }


    //  public void amad(View v){ playSoundFile(R.raw.amad); }
    //Find all views and set Tag to all draggable views
    private void findViews() {
        textView1 = (TextView) findViewById(R.id.d1);
        textView1.setTag(TEXT_VIEW_TAG);
        textView2 = (TextView) findViewById(R.id.e1);
        textView2.setTag(TEXT_VIEW_TAG);

        textView3 = (TextView) findViewById(R.id.d2);
        textView3.setTag(TEXT_VIEW_TAG);
        textView4 = (TextView) findViewById(R.id.e2);
        textView4.setTag(TEXT_VIEW_TAG);

    }


    //Implement long click and drag listener
    private void implementEvents() {
        //add or remove any view that you don't want to be dragged
        textView1.setOnLongClickListener(this);
        textView2.setOnLongClickListener(this);
        textView3.setOnLongClickListener(this);
        textView4.setOnLongClickListener(this);

        //add or remove any layout view that you don't want to accept dragged view
        findViewById(R.id.top1).setOnDragListener(this);
        findViewById(R.id.left_layout1).setOnDragListener(this);
        findViewById(R.id.right_layout1).setOnDragListener(this);

        findViewById(R.id.top2).setOnDragListener(this);
        findViewById(R.id.left_layout2).setOnDragListener(this);
        findViewById(R.id.right_layout2).setOnDragListener(this);
    }

    @Override
    public boolean onLongClick(View view) {

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
                    Toast.makeText(this, "The drop was handled." + score, Toast.LENGTH_SHORT).show();
                    if (incremented && score < 4)
                        score++;
                    else {
                        incremented = false;
                    }
                } else
                    Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_SHORT).show();


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


