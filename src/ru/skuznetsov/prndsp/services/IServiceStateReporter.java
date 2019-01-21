package ru.skuznetsov.prndsp.services;

/**
 * IServiceStateReporter reports current service state for time-consuming services
 * IServiceStateReporter.getCurrentState() immediately returns current state of
 * long processing service
 */
public interface IServiceStateReporter
{
    public IServiceState getCurrentState();
}
