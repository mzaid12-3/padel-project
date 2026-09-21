package com.padel.draft.api;

import java.util.List;

public record ImportResponse(int importedCount, List<PlayerResponse> players, List<ImportError> errors) {
    public record ImportError(long row, String field, String message) { }
}
