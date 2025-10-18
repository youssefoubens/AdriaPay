package com.youssef.beneficiaireservice.service;



import com.youssef.beneficiaireservice.dto.BeneficiaireRequestDTO;
import com.youssef.beneficiaireservice.dto.BeneficiaireResponseDTO;
import com.youssef.beneficiaireservice.dto.BeneficiaireUpdateDTO;
import com.youssef.beneficiaireservice.enums.TypeBeneficiaire;

import java.util.List;

/**
 * Interface de service pour la gestion des bénéficiaires
 * Définit toutes les opérations métier disponibles
 */
public interface IBeneficiaireService {

    // ==================== Opérations CRUD de base ====================

    /**
     * Crée un nouveau bénéficiaire
     *
     * @param dto Les données du bénéficiaire à créer
     * @return Le bénéficiaire créé avec son ID généré
     * @throws DuplicateRibException si le RIB existe déjà
     */
    BeneficiaireResponseDTO create(BeneficiaireRequestDTO dto);

    /**
     * Récupère un bénéficiaire par son identifiant
     *
     * @param id L'identifiant du bénéficiaire
     * @return Le bénéficiaire trouvé
     * @throws BeneficiaireNotFoundException si le bénéficiaire n'existe pas
     */
    BeneficiaireResponseDTO getById(Long id);

    /**
     * Récupère un bénéficiaire par son RIB
     *
     * @param rib Le RIB du bénéficiaire (24 chiffres)
     * @return Le bénéficiaire trouvé
     * @throws BeneficiaireNotFoundException si le bénéficiaire n'existe pas
     */
    BeneficiaireResponseDTO getByRib(String rib);

    /**
     * Récupère tous les bénéficiaires
     *
     * @return Liste de tous les bénéficiaires
     */
    List<BeneficiaireResponseDTO> getAll();

    /**
     * Met à jour un bénéficiaire existant (mise à jour partielle)
     * Seuls les champs non-null du DTO sont mis à jour
     *
     * @param id L'identifiant du bénéficiaire à modifier
     * @param dto Les nouvelles données (les champs null sont ignorés)
     * @return Le bénéficiaire mis à jour
     * @throws BeneficiaireNotFoundException si le bénéficiaire n'existe pas
     */
    BeneficiaireResponseDTO update(Long id, BeneficiaireUpdateDTO dto);

    /**
     * Supprime définitivement un bénéficiaire
     *
     * @param id L'identifiant du bénéficiaire à supprimer
     * @throws BeneficiaireNotFoundException si le bénéficiaire n'existe pas
     */
    void delete(Long id);

    // ==================== Opérations de recherche et filtrage ====================

    /**
     * Récupère les bénéficiaires par type
     *
     * @param type Le type de bénéficiaire (PHYSIQUE ou MORALE)
     * @return Liste des bénéficiaires du type spécifié
     */
    List<BeneficiaireResponseDTO> getByType(TypeBeneficiaire type);

    /**
     * Récupère tous les bénéficiaires actifs
     *
     * @return Liste des bénéficiaires actifs
     */
    List<BeneficiaireResponseDTO> getActifs();

    /**
     * Récupère tous les bénéficiaires inactifs
     *
     * @return Liste des bénéficiaires inactifs
     */
    List<BeneficiaireResponseDTO> getInactifs();

    /**
     * Recherche des bénéficiaires par mot-clé dans le nom ou prénom
     *
     * @param keyword Le mot-clé de recherche
     * @return Liste des bénéficiaires correspondants
     */
    List<BeneficiaireResponseDTO> search(String keyword);

    /**
     * Recherche avancée avec filtres multiples
     *
     * @param type Filtrer par type (optionnel)
     * @param actif Filtrer par statut actif (optionnel)
     * @param keyword Rechercher dans nom/prénom (optionnel)
     * @return Liste des bénéficiaires correspondant aux critères
     */
    List<BeneficiaireResponseDTO> searchWithFilters(
            TypeBeneficiaire type,
            Boolean actif,
            String keyword
    );

    // ==================== Opérations sur le statut ====================

    /**
     * Désactive un bénéficiaire (soft delete)
     * Le bénéficiaire reste en base mais n'est plus actif
     *
     * @param id L'identifiant du bénéficiaire à désactiver
     * @throws BeneficiaireNotFoundException si le bénéficiaire n'existe pas
     */
    void deactivate(Long id);

    /**
     * Active un bénéficiaire précédemment désactivé
     *
     * @param id L'identifiant du bénéficiaire à activer
     * @throws BeneficiaireNotFoundException si le bénéficiaire n'existe pas
     */
    void activate(Long id);

    // ==================== Opérations de vérification ====================

    /**
     * Vérifie si un RIB existe déjà dans le système
     *
     * @param rib Le RIB à vérifier
     * @return true si le RIB existe, false sinon
     */
    boolean existsByRib(String rib);

    /**
     * Vérifie si un bénéficiaire existe par son ID
     *
     * @param id L'identifiant à vérifier
     * @return true si le bénéficiaire existe, false sinon
     */
    boolean existsById(Long id);

    // ==================== Opérations statistiques ====================

    /**
     * Compte le nombre total de bénéficiaires
     *
     * @return Le nombre total de bénéficiaires
     */
    long count();

    /**
     * Compte le nombre de bénéficiaires par type
     *
     * @param type Le type de bénéficiaire
     * @return Le nombre de bénéficiaires du type spécifié
     */
    long countByType(TypeBeneficiaire type);

    /**
     * Compte le nombre de bénéficiaires actifs
     *
     * @return Le nombre de bénéficiaires actifs
     */
    long countActifs();
}
