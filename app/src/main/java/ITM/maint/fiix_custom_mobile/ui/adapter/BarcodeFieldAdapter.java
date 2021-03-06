/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ITM.maint.fiix_custom_mobile.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.BarcodeField;

/** Presents a list of field info in the detected barcode. */
public class BarcodeFieldAdapter extends RecyclerView.Adapter<BarcodeFieldAdapter.BarcodeFieldViewHolder> {

  static class BarcodeFieldViewHolder extends RecyclerView.ViewHolder {

    static BarcodeFieldViewHolder create(ViewGroup parent) {
      View view =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.barcode_field, parent, false);
      return new BarcodeFieldViewHolder(view);
    }

    private final TextView labelView;
    private final TextView valueView;

    private BarcodeFieldViewHolder(View view) {
      super(view);
      labelView = view.findViewById(R.id.barcode_field_label);
      valueView = view.findViewById(R.id.barcode_field_value);
    }

    void bindBarcodeField(BarcodeField barcodeField) {
      labelView.setText(barcodeField.getLabel());
      valueView.setText(barcodeField.getValue());
    }
  }

  private final List<BarcodeField> barcodeFieldList;

  public BarcodeFieldAdapter(List<BarcodeField> barcodeFieldList) {
    this.barcodeFieldList = barcodeFieldList;
  }

  @Override
  @NonNull
  public BarcodeFieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return BarcodeFieldViewHolder.create(parent);
  }

  @Override
  public void onBindViewHolder(@NonNull BarcodeFieldViewHolder holder, int position) {
    holder.bindBarcodeField(barcodeFieldList.get(position));
  }

  @Override
  public int getItemCount() {
    return barcodeFieldList.size();
  }
}
