// Generated by Dagger (https://dagger.dev).
package com.example.demoaerisproject.view.settings;

import com.example.demoaerisproject.data.preferenceStore.PrefStoreRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class PrefViewModel_Factory implements Factory<PrefViewModel> {
  private final Provider<PrefStoreRepository> prefStoreRepositoryProvider;

  public PrefViewModel_Factory(Provider<PrefStoreRepository> prefStoreRepositoryProvider) {
    this.prefStoreRepositoryProvider = prefStoreRepositoryProvider;
  }

  @Override
  public PrefViewModel get() {
    return newInstance(prefStoreRepositoryProvider.get());
  }

  public static PrefViewModel_Factory create(
      Provider<PrefStoreRepository> prefStoreRepositoryProvider) {
    return new PrefViewModel_Factory(prefStoreRepositoryProvider);
  }

  public static PrefViewModel newInstance(PrefStoreRepository prefStoreRepository) {
    return new PrefViewModel(prefStoreRepository);
  }
}
