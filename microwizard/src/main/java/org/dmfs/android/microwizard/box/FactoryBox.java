/*
 * Copyright (c) 2017 dmfs GmbH
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

package org.dmfs.android.microwizard.box;

import android.os.Parcel;

import org.dmfs.jems.function.Function;
import org.dmfs.jems.single.Single;


/**
 * A Box of a class which doesn't have any data to parcel and which can be generated by a factory.
 * <p>
 * TODO: move to boxed-bolts
 *
 * @author Marten Gajda
 */
public abstract class FactoryBox<T> implements Box<T>
{
    private final Single<T> mFactory;


    public FactoryBox(Single<T> factory)
    {
        mFactory = factory;
    }


    @Override
    public final int describeContents()
    {
        return 0;
    }


    @Override
    public final void writeToParcel(Parcel dest, int flags)
    {
        // nothing to write
    }


    @Override
    public final T value()
    {
        return mFactory.value();
    }


    public final static class FactoryBoxCreator<T> implements Creator<T>
    {
        private final Single<T> mFactory;
        private final Function<Integer, T[]> mArrayFactory;


        public FactoryBoxCreator(Single<T> factory, Function<Integer, T[]> arrayFactory)
        {
            mFactory = factory;
            mArrayFactory = arrayFactory;
        }


        @Override
        public T createFromParcel(Parcel source)
        {
            return mFactory.value();
        }


        @Override
        public T[] newArray(int size)
        {
            return mArrayFactory.value(size);
        }
    }
}