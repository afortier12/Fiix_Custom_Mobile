package ITM.maint.fiix_custom_mobile.data.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

import ITM.maint.fiix_custom_mobile.data.model.entity.Asset;
import ITM.maint.fiix_custom_mobile.data.model.entity.AssetCategory;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface IAssetDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insert(List<Asset> assets);

    @Query("SELECT category_id FROM asset_table WHERE id = :id")
    Single<AssetCategory> getAssetCategory(String id);

    @Query("SELECT * from asset_table ORDER BY id ASC")
    Single<List<Asset>> getAssets();

    @Query("SELECT * from asset_table WHERE id in (:asset_ids)")
    Single<List<Asset>> getAssetFromIds(List<Integer> asset_ids);

    @Query("SELECT * from asset_table WHERE id in (:asset_ids)")
    Single<List<AssetCategory>> getAssetCategoriesFromIds(List<Integer> asset_ids);

    @Query("SELECT * from asset_category_table ORDER BY id ASC")
    Single<List<AssetCategory>> getAssetCategories();

    @Query("DELETE FROM asset_table")
    Completable deleteAll();

}
