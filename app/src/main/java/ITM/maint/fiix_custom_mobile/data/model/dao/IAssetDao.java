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
    Completable insertAssets(List<Asset> assets);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertAssetCategories(List<AssetCategory> assetCategories);

    @Query("SELECT * from asset_table ORDER BY id ASC")
    Single<List<Asset>> getAssets();

    @Query("SELECT * from asset_table WHERE type = :type")
    Single<List<Asset>> getAssetsByType(int type);

    @Query("SELECT * from asset_table WHERE id in (:asset_ids)")
    Single<List<Asset>> getAssetFromIds(List<Integer> asset_ids);

    @Query("SELECT * FROM asset_category_table WHERE id = :id")
    Single<AssetCategory> getAssetCategory(int id);

    @Query("SELECT * from asset_category_table WHERE id in (:category_ids)")
    Single<List<AssetCategory>> getAssetCategoriesFromIds(List<Integer> category_ids);

    @Query("SELECT * from asset_category_table ORDER BY id ASC")
    Single<List<AssetCategory>> getAssetCategories();

    @Query("SELECT * from asset_category_table WHERE parentId = :parent_id")
    Single<List<AssetCategory>> getAssetCategoriesByParentId(int parent_id);

    @Query("DELETE FROM asset_table")
    Completable deleteAll();

}
