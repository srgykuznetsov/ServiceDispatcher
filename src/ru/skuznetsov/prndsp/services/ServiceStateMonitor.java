package ru.skuznetsov.prndsp.services;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Performs non-blocking monitoring of current service state through
 * internal IServiceStateReporter instance at a specific schedule;
 *
 * Notifies IStateUpdatedListener whenever state update is detected
 */
public class ServiceStateMonitor
{
    /** Used to update service state data at a specific schedule*/
    private final Timer m_timer;
    private final long m_lInitialDelay;
    private final long m_lMinExtraDelay;
    private final long m_lMaxExtraDelay;

    /**Reports currently executed service state*/
    private final IServiceStateReporter m_stateReporter;
    /**Receives notification when state is updated*/
    private final IStateUpdatedListener m_stateUpdatedListener;

    private IServiceState m_currentState;
    private boolean m_bFirstState;
    private long m_lCurrentDelay;

    public ServiceStateMonitor(
            Timer timer,
            long lInitialDelay,
            long lMinExtraDelay,
            long lMaxExtraDelay,
            IServiceStateReporter stateReporter,
            IStateUpdatedListener stateUpdatedListener,
            IServiceState initialState)
    {
        m_timer = timer;
        m_lInitialDelay  = lInitialDelay;
        m_lMinExtraDelay = lMinExtraDelay;
        m_lMaxExtraDelay = lMaxExtraDelay;

        m_stateReporter = stateReporter;
        m_stateUpdatedListener = stateUpdatedListener;

        m_currentState = initialState;
        m_bFirstState = true;
        m_lCurrentDelay = lInitialDelay;
    }

    public void start()
    {
        scheduleNextRequest();

        if (m_bFirstState)
        {
            m_bFirstState = false;
            m_lCurrentDelay = m_lMinExtraDelay;
        }
        else
        {
            if (m_lCurrentDelay < m_lMaxExtraDelay)
            {
                m_lCurrentDelay *= 2;
            }
            if (m_lCurrentDelay > m_lMaxExtraDelay)
            {
                m_lCurrentDelay = m_lMaxExtraDelay;
            }
        }
    }

    void getNewState()
    {
        IServiceState newState = m_stateReporter.getCurrentState();
        if (!m_currentState.equals(newState))
        {
            m_currentState = newState;
            m_stateUpdatedListener.onUpdateState(newState);
        }

        scheduleNextRequest();
    }

    void scheduleNextRequest()
    {
        TimerTask task = new TimerTask()
        {
            public void run()
            {
                getNewState();
            }
        };

        m_timer.schedule(task, m_lCurrentDelay);
    }


}
