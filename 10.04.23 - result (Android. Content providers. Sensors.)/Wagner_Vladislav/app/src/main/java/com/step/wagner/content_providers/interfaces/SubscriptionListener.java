package com.step.wagner.content_providers.interfaces;

import java.util.Date;

public interface SubscriptionListener {

    void onOkClickListener(int id, Date dateStart, int publicationId, int duration);

}
