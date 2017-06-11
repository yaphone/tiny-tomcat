package cn.zhouyafeng.catalina.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zhouyafeng.catalina.Lifecycle;
import cn.zhouyafeng.catalina.LifecycleException;
import cn.zhouyafeng.catalina.LifecycleListener;
import cn.zhouyafeng.catalina.LifecycleState;
import cn.zhouyafeng.tomcat.util.ExceptionUtils;

public abstract class LifecycleBase implements Lifecycle {
	private static Logger LOG = LoggerFactory.getLogger(LifecycleBase.class);

	private final LifecycleSupport lifecycle = new LifecycleSupport(this);
	private volatile LifecycleState state = LifecycleState.NEW;

	protected abstract void initInternal() throws LifecycleException;

	protected abstract void startInternal() throws LifecycleException;

	protected abstract void destroyInternal() throws LifecycleException;

	protected abstract void stopInternal() throws LifecycleException;

	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		lifecycle.addLifecycleListener(listener);
	}

	@Override
	public LifecycleListener[] findLifecycleListeners() {
		return lifecycle.findLifecycleListeners();
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		lifecycle.removeLifecycleEvent(listener);
	}

	protected void fireLifecycleEvent(String type, Object data) {
		lifecycle.fireLifecycleEvent(type, data);
	}

	@Override
	public final synchronized void start() throws LifecycleException {
		if (LifecycleState.STARTING_PREP.equals(state) || LifecycleState.STARTING.equals(state)
				|| LifecycleState.STARTED.equals(state)) {
			if (LOG.isDebugEnabled()) {
				Exception e = new LifecycleException();
				LOG.debug("lifecycleBase.alreadyStarted", e);
			} else if (LOG.isInfoEnabled()) {
				LOG.info("lifecycleBase.alreadyStarted");
			}
			return;
		}

		if (state.equals(LifecycleState.NEW)) {
			init();
		} else if (state.equals(LifecycleState.FAILED)) {
			stop();
		} else if (!state.equals(LifecycleState.INITIALIZED) && !state.equals(LifecycleState.STOPPED)) {
			invalidTransition(Lifecycle.BEFORE_START_EVENT);
		}

		try {
			setStateInternal(LifecycleState.STARTING_PREP, null, false);
			startInternal();
			if (state.equals(LifecycleState.FAILED)) {
				// This is a 'controlled' failure. The component put itself into
				// the
				// FAILED state so call stop() to complete the clean-up.
				stop();
			} else if (!state.equals(LifecycleState.STARTING)) {
				// Shouldn't be necessary but acts as a check that sub-classes
				// are
				// doing what they are supposed to.
				invalidTransition(Lifecycle.AFTER_START_EVENT);
			} else {
				setStateInternal(LifecycleState.STARTED, null, false);
			}
		} catch (Throwable t) {
			// This is an 'uncontrolled' failure so put the component into the
			// FAILED state and throw an exception.
			ExceptionUtils.handleThrowable(t);
			setStateInternal(LifecycleState.FAILED, null, false);
			throw new LifecycleException("lifecycleBase.startFail", t);
		}
	}

	@Override
	public final synchronized void stop() throws LifecycleException {

		if (LifecycleState.STOPPING_PREP.equals(state) || LifecycleState.STOPPING.equals(state)
				|| LifecycleState.STOPPED.equals(state)) {

			if (LOG.isDebugEnabled()) {
				Exception e = new LifecycleException();
				LOG.debug("lifecycleBase.alreadyStopped", toString(), e);
			} else if (LOG.isInfoEnabled()) {
				LOG.info("lifecycleBase.alreadyStopped", toString());
			}

			return;
		}

		if (state.equals(LifecycleState.NEW)) {
			state = LifecycleState.STOPPED;
			return;
		}

		if (!state.equals(LifecycleState.STARTED) && !state.equals(LifecycleState.FAILED)) {
			invalidTransition(Lifecycle.BEFORE_STOP_EVENT);
		}

		try {
			if (state.equals(LifecycleState.FAILED)) {
				// Don't transition to STOPPING_PREP as that would briefly mark
				// the
				// component as available but do ensure the BEFORE_STOP_EVENT is
				// fired
				fireLifecycleEvent(BEFORE_STOP_EVENT, null);
			} else {
				setStateInternal(LifecycleState.STOPPING_PREP, null, false);
			}

			stopInternal();

			// Shouldn't be necessary but acts as a check that sub-classes are
			// doing what they are supposed to.
			if (!state.equals(LifecycleState.STOPPING) && !state.equals(LifecycleState.FAILED)) {
				invalidTransition(Lifecycle.AFTER_STOP_EVENT);
			}

			setStateInternal(LifecycleState.STOPPED, null, false);
		} catch (Throwable t) {
			ExceptionUtils.handleThrowable(t);
			setStateInternal(LifecycleState.FAILED, null, false);
			throw new LifecycleException("lifecycleBase.stopFail", t);
		} finally {
			if (this instanceof Lifecycle.SingleUse) {
				// Complete stop process first
				setStateInternal(LifecycleState.STOPPED, null, false);
				destroy();
			}
		}
	}

	@Override
	public final synchronized void destroy() throws LifecycleException {
		if (LifecycleState.FAILED.equals(state)) {
			try {
				// Triggers clean-up
				stop();
			} catch (LifecycleException e) {
				// Just LOG. Still want to destroy.
				LOG.warn("lifecycleBase.destroyStopFail", toString(), e);
			}
		}

		if (LifecycleState.DESTROYING.equals(state) || LifecycleState.DESTROYED.equals(state)) {

			if (LOG.isDebugEnabled()) {
				Exception e = new LifecycleException();
				LOG.debug("lifecycleBase.alreadyDestroyed", toString(), e);
			} else if (LOG.isInfoEnabled() && !(this instanceof Lifecycle.SingleUse)) {
				// Rather than have every component that might need to call
				// destroy() check for SingleUse, don't LOG an info message if
				// multiple calls are made to destroy()
				LOG.info("lifecycleBase.alreadyDestroyed", toString());
			}

			return;
		}

		if (!state.equals(LifecycleState.STOPPED) && !state.equals(LifecycleState.FAILED)
				&& !state.equals(LifecycleState.NEW) && !state.equals(LifecycleState.INITIALIZED)) {
			invalidTransition(Lifecycle.BEFORE_DESTROY_EVENT);
		}

		try {
			setStateInternal(LifecycleState.DESTROYING, null, false);
			destroyInternal();
			setStateInternal(LifecycleState.DESTROYED, null, false);
		} catch (Throwable t) {
			ExceptionUtils.handleThrowable(t);
			setStateInternal(LifecycleState.FAILED, null, false);
			throw new LifecycleException("lifecycleBase.destroyFail", t);
		}
	}

	@Override
	public LifecycleState getState() {
		return state;
	}

	@Override
	public String getStateName() {
		return getState().toString();
	}

	protected synchronized void setState(LifecycleState state) throws LifecycleException {
		setStateInternal(state, null, true);
	}

	protected synchronized void setState(LifecycleState state, Object data) throws LifecycleException {
		setStateInternal(state, data, true);
	}

	private synchronized void setStateInternal(LifecycleState state, Object data, boolean check)
			throws LifecycleException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("lifecycleBase.setState", state);
		}

		if (check) {
			if (state == null) {
				invalidTransition("null");
				return;
			}

			if (!(state == LifecycleState.FAILED
					|| (this.state == LifecycleState.STARTING_PREP && state == LifecycleState.STARTING)
					|| (this.state == LifecycleState.STOPPING_PREP && state == LifecycleState.STOPPING)
					|| (this.state == LifecycleState.FAILED && state == LifecycleState.STOPPING))) {
				invalidTransition(state.name());
			}
		}

		this.state = state;
		String lifecycleEvent = state.getLifecycleEvent();
		if (lifecycleEvent != null) {
			fireLifecycleEvent(lifecycleEvent, data);
		}
	}

	private void invalidTransition(String type) throws LifecycleException {
		throw new LifecycleException("lifecycleBase.invalidTransition");
	}

}
