package com.gabriel.pocketstatement.domain.usecase

data class ReceiptUseCases(
    val getReceiptsUseCase: GetReceiptsUseCase,
    val deleteReceiptUseCase: DeleteReceiptUseCase,
    val saveReceiptUseCase: SaveReceiptUseCase,
    val getReceiptByIdUseCase: GetReceiptByIdUseCase,
    val textRecognitionUseCase: TextRecognitionUseCase
)