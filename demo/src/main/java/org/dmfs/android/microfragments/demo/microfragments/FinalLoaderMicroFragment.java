/*
 * Copyright 2016 dmfs GmbH
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dmfs.android.microfragments.demo.microfragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dmfs.android.microfragments.MicroFragment;
import org.dmfs.android.microfragments.MicroFragmentHost;
import org.dmfs.android.microfragments.demo.R;
import org.dmfs.android.microfragments.transitions.ResetFragmentTransition;


/**
 * @author Marten Gajda
 */
public final class FinalLoaderMicroFragment implements MicroFragment<Void>
{
    @Override
    public String title(Context context)
    {
        return "Loading again …";
    }


    @NonNull
    @Override
    public Fragment fragment(@NonNull Context context, @NonNull MicroFragmentHost host)
    {
        Fragment fragment = new LoadFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MICRO_FRAGMENT, this);
        args.putParcelable("host", host);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Void parameters()
    {
        return null;
    }


    @Override
    public boolean skipOnBack()
    {
        return true;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {

    }


    public final static Creator<FinalLoaderMicroFragment> CREATOR = new Creator<FinalLoaderMicroFragment>()
    {
        @Override
        public FinalLoaderMicroFragment createFromParcel(Parcel source)
        {
            return new FinalLoaderMicroFragment();
        }


        @Override
        public FinalLoaderMicroFragment[] newArray(int size)
        {
            return new FinalLoaderMicroFragment[size];
        }
    };


    public static class LoadFragment extends Fragment
    {
        private final static int DELAY_WAIT_MESSAGE = 2500;
        private final Handler mHandler = new Handler();


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            View result = inflater.inflate(R.layout.fragment_loading, container, false);
            result.findViewById(android.R.id.message).animate().alpha(1).setStartDelay(DELAY_WAIT_MESSAGE);
            return result;
        }


        @Override
        public void onStart()
        {
            super.onStart();
            new Thread(mFakeLoader).start();
        }


        private final Runnable mFakeLoader = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(DELAY_WAIT_MESSAGE);
                }
                catch (InterruptedException e)
                {
                }

                if (isAdded() && isResumed())
                {
                    MicroFragmentHost host = getArguments().getParcelable("host");
                    host.execute(getActivity(), new ResetFragmentTransition(new LastMicroFragment()));
                }
            }
        };
    }
}
