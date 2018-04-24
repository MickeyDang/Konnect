package mmd.konnect.Listener;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

//only handles single clicks
public class RecyclerClickListener implements RecyclerView.OnItemTouchListener {

    private ItemClickListener mListener;

    public RecyclerClickListener(ItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if (childView != null) {
            mListener.onItemClicked(childView, rv.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        //ignore
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        //ignore
    }

    public interface ItemClickListener {
        void onItemClicked(View view, int position);
    }
}
