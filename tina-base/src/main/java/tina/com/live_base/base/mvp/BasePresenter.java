package tina.com.live_base.base.mvp;

/**
 * @author Ragnar
 */
public abstract class BasePresenter {

    /**
     * @description view is attached
     */
    public abstract void onAttach();

    /**
     * @description view detached
     */
    public abstract void onDetach();

    public void onViewCreated() {
    }

    public void onResume() {
    }

    public void onPause() {
    }

}
