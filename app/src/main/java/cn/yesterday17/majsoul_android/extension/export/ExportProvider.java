package cn.yesterday17.majsoul_android.extension.export;

import cn.yesterday17.majsoul_android.extension.metadata.IMetadata;

public interface ExportProvider {
    void export(IMetadata toExport);
}
