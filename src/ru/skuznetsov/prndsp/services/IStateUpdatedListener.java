package ru.skuznetsov.prndsp.services;

/**
 * IStateUpdatedListener.onUpdateState is called by ServiceStateMonitor when it detects that
 * state was updated
 */
public interface IStateUpdatedListener
{
    /**
     * called by ServiceStateMonitor when it detects that state was updated
     * @param updatedState - new current service state
     */
    public void onUpdateState(IServiceState updatedState);
}
